package com.v380.client.domain

import android.graphics.SurfaceTexture
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.view.Surface
import com.v380.client.data.network.StreamClient
import kotlinx.coroutines.*
import java.nio.ByteBuffer

class StreamManager(
    private val streamUrl: String,
    private val onFrame: (() -> Unit)? = null,
    private val onError: ((String) -> Unit)? = null,
) {
    private var mediaCodec: MediaCodec? = null
    private var surface: Surface? = null
    private var streamClient: StreamClient? = null
    private var job: Job? = null
    private var isRunning = false

    private val bufferSize = 1024 * 1024
    private val inputBuffers = Array(256) { ByteArray(bufferSize) }
    private val csdSps = byteArrayOf(
        0x00, 0x00, 0x00, 0x01, 0x42, 0x01, 0x01, 0x01, 0x60, 0x00, 0x00, 0x03, 0x00, 0x90, 0x00, 0x00,
        0x03, 0x00, 0x00, 0x03, 0x00, 0x78, 0xA0, 0x02, 0x80
    )
    private val csdPps = byteArrayOf(
        0x00, 0x00, 0x00, 0x01, 0x44, 0x01, 0xC0, 0x72, 0xB0, 0x20, 0x10, 0x10, 0x10
    )

    fun setSurface(surfaceTexture: SurfaceTexture?) {
        surface = surfaceTexture?.let { Surface(it) }
    }

    fun start() {
        if (isRunning) return
        isRunning = true
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                initMediaCodec()
                streamClient = StreamClient(streamUrl)
                val stream = streamClient?.connect()?.getOrThrow() ?: return@launch
                val buffer = ByteArray(bufferSize)
                var offset = 0

                while (isRunning && isActive) {
                    val bytesRead = stream.read(buffer, offset, buffer.size - offset)
                    if (bytesRead <= 0) break
                    offset += bytesRead
                    val startCode = findStartCode(buffer, offset)
                    if (startCode < 0) continue
                    val remaining = offset - startCode
                    val chunk = buffer.copyOfRange(startCode, startCode + remaining)
                    feedDecoder(chunk)
                    offset = 0
                }
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Stream error")
            } finally {
                cleanup()
            }
        }
    }

    fun stop() {
        isRunning = false
        job?.cancel()
        cleanup()
    }

    private fun initMediaCodec() {
        val format = MediaFormat.createVideoFormat(MediaFormat.MIME_TYPE_HEVC, 640, 720)
        format.setInteger(MediaFormat.KEY_BIT_RATE, 1000000)
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 30)
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)

        val csd0 = ByteArray(csdSps.size + csdPps.size)
        System.arraycopy(csdSps, 0, csd0, 0, csdSps.size)
        System.arraycopy(csdPps, 0, csd0, csdSps.size, csdPps.size)
        format.setByteBuffer("csd-0", ByteBuffer.wrap(csd0))

        mediaCodec = MediaCodec.createDecoderByType(MediaFormat.MIME_TYPE_HEVC)
        mediaCodec?.configure(format, surface, null, 0)
        mediaCodec?.start()
    }

    private fun feedDecoder(data: ByteArray) {
        val codec = mediaCodec ?: return
        val inputIndex = codec.dequeueInputBuffer(10000)
        if (inputIndex < 0) return
        val inputBuffer = codec.getInputBuffer(inputIndex) ?: return
        inputBuffer.clear()
        inputBuffer.put(data)
        codec.queueInputBuffer(inputIndex, 0, data.size, System.nanoTime() / 1000, 0)

        val bufferInfo = MediaCodec.BufferInfo()
        var outputIndex = codec.dequeueOutputBuffer(bufferInfo, 10000)
        while (outputIndex >= 0) {
            codec.releaseOutputBuffer(outputIndex, true)
            onFrame?.invoke()
            outputIndex = codec.dequeueOutputBuffer(bufferInfo, 10000)
        }
    }

    private fun findStartCode(data: ByteArray, len: Int): Int {
        for (i in 0..len - 4) {
            if (data[i] == 0.toByte() && data[i + 1] == 0.toByte() && data[i + 2] == 0.toByte() && data[i + 3] == 1.toByte()) return i
            if (data[i] == 0.toByte() && data[i + 1] == 0.toByte() && data[i + 2] == 1.toByte()) return i
        }
        return -1
    }

    private fun cleanup() {
        try { mediaCodec?.stop() } catch (_: Exception) {}
        try { mediaCodec?.release() } catch (_: Exception) {}
        mediaCodec = null
        streamClient?.disconnect()
        streamClient = null
    }
}