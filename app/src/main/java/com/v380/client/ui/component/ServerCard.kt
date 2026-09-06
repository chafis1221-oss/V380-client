package com.v380.client.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v380.client.data.model.Server
import com.v380.client.ui.theme.V380Colors

@Composable
fun ServerCard(
    server: Server,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(V380Colors.surface, RoundedCornerShape(4.dp))
            .border(1.dp, V380Colors.border, RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        if (server.isActive) V380Colors.success else V380Colors.textDim,
                        RoundedCornerShape(4.dp)
                    )
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = server.name,
                    color = V380Colors.text,
                    fontSize = 14.sp,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${server.ip}:${server.port}",
                    color = V380Colors.textMuted,
                    fontSize = 12.sp,
                )
                if (server.isActive) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "${server.fps}fps | ${server.resolution} | ${server.quality.uppercase()}",
                        color = V380Colors.textDim,
                        fontSize = 11.sp,
                    )
                }
            }
            if (server.isActive) {
                Box(
                    modifier = Modifier
                        .background(V380Colors.success.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                        .border(1.dp, V380Colors.success.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text("LIVE", color = V380Colors.success, fontSize = 10.sp, letterSpacing = 0.5.sp)
                }
            }
        }
    }
}