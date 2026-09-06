package com.v380.client.ui.component

import android.graphics.SurfaceTexture
import android.view.TextureView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.v380.client.domain.StreamManager

@Composable
fun VideoView(
    streamUrl: String,
    modifier: Modifier = Modifier,
    onFrame: (() -> Unit)? = null,
    onError: ((String) -> Unit)? = null,
) {
    var streamManager by remember { mutableStateOf<StreamManager?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            TextureView(ctx).apply {
                surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, w: Int, h: Int) {
                        val sm = StreamManager(streamUrl, onFrame, onError)
                        sm.setSurface(surface)
                        sm.start()
                        streamManager = sm
                    }

                    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, w: Int, h: Int) {}
                    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                        streamManager?.stop()
                        streamManager = null
                        return true
                    }

                    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
                }
            }
        }
    )
}