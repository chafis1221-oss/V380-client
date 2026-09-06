package com.v380.client.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v380.client.data.model.Server
import com.v380.client.data.network.CameraApi
import com.v380.client.ui.component.ControlRow
import com.v380.client.ui.component.PtzPad
import com.v380.client.ui.component.VideoView
import com.v380.client.ui.theme.V380Colors
import kotlinx.coroutines.launch

@Composable
fun CameraViewerScreen(
    server: Server,
    onBack: () -> Unit,
) {
    val baseUrl = "http://${server.ip}:${server.port}"
    val api = remember { CameraApi(baseUrl) }
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var isConnected by remember { mutableStateOf(false) }
    var fps by remember { mutableIntStateOf(0) }
    var frameCount by remember { mutableIntStateOf(0) }
    var lastFrameTime by remember { mutableLongStateOf(System.nanoTime()) }

    Box(modifier = Modifier.fillMaxSize().background(V380Colors.bg)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TopBar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(V380Colors.surface)
                    .border(1.dp, V380Colors.border)
                    .padding(horizontal = 12.dp),
            ) {
                Text(
                    text = "←",
                    color = V380Colors.textMuted,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable(onClick = onBack),
                )
                Spacer(Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(
                            if (isConnected) V380Colors.success else V380Colors.textDim,
                            RoundedCornerShape(3.dp)
                        )
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = server.name,
                    color = V380Colors.text,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f),
                )
                if (isConnected) {
                    Text(
                        text = "${fps}fps",
                        color = V380Colors.textMuted,
                        fontSize = 11.sp,
                    )
                }
            }

            if (isLandscape) {
                // Landscape: video fullscreen, controls overlay
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    VideoView(
                        streamUrl = api.buildStreamUrl(),
                        modifier = Modifier.fillMaxSize(),
                        onFrame = {
                            frameCount++
                            val now = System.nanoTime()
                            if (now - lastFrameTime > 1_000_000_000) {
                                fps = frameCount
                                frameCount = 0
                                lastFrameTime = now
                            }
                            if (!isConnected) isConnected = true
                        },
                        onError = { isConnected = false },
                    )
                }
            } else {
                // Portrait: video + controls
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(V380Colors.bg),
                ) {
                    VideoView(
                        streamUrl = api.buildStreamUrl(),
                        modifier = Modifier.fillMaxSize(),
                        onFrame = {
                            frameCount++
                            val now = System.nanoTime()
                            if (now - lastFrameTime > 1_000_000_000) {
                                fps = frameCount
                                frameCount = 0
                                lastFrameTime = now
                            }
                            if (!isConnected) isConnected = true
                        },
                        onError = { isConnected = false },
                    )
                }

                // Controls
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(V380Colors.surface)
                        .border(1.dp, V380Colors.border)
                        .padding(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        PtzPad(
                            onDirection = { dir ->
                                scope.launch {
                                    runCatching { api.ptz(dir) }
                                }
                            },
                        )
                        Spacer(Modifier.width(16.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            ControlRow(
                                label = "Light",
                                buttons = listOf("On" to false, "Off" to false, "Auto" to false),
                                onButtonClick = { mode ->
                                    scope.launch { runCatching { api.light(mode) } }
                                },
                            )
                            ControlRow(
                                label = "Image",
                                buttons = listOf("Color" to false, "BW" to false, "Auto" to false, "Flip" to false),
                                onButtonClick = { mode ->
                                    scope.launch { runCatching { api.image(mode) } }
                                },
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = if (isConnected) "● Live" else "○ Disconnected",
                            color = if (isConnected) V380Colors.success else V380Colors.textDim,
                            fontSize = 11.sp,
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "${server.ip}:${server.port}",
                            color = V380Colors.textDim,
                            fontSize = 11.sp,
                        )
                    }
                }
            }
        }
    }
}