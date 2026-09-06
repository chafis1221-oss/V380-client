package com.v380.client.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream
import java.util.concurrent.TimeUnit

class StreamClient(private val streamUrl: String) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.SECONDS)
        .build()

    private var inputStream: InputStream? = null
    private var isRunning = false

    suspend fun connect(): Result<InputStream> = withContext(Dispatchers.IO) {
        runCatching {
            val request = Request.Builder().url(streamUrl).build()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) throw Exception("Stream HTTP ${response.code}")
            response.body?.byteStream() ?: throw Exception("No stream body")
        }
    }

    suspend fun read(buffer: ByteArray): Int = withContext(Dispatchers.IO) {
        inputStream?.read(buffer) ?: -1
    }

    fun disconnect() {
        isRunning = false
        try { inputStream?.close() } catch (_: Exception) {}
    }
}