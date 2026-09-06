package com.v380.client.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v380.client.data.repository.ServerRepository
import com.v380.client.ui.theme.V380Colors
import kotlinx.coroutines.launch

@Composable
fun AddServerDialog(
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    repository: ServerRepository,
) {
    var name by remember { mutableStateOf("") }
    var ip by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("8080") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(V380Colors.surface, RoundedCornerShape(4.dp))
            .border(1.dp, V380Colors.border, RoundedCornerShape(4.dp))
            .padding(16.dp),
    ) {
        Text("Add Server", color = V380Colors.text, fontSize = 16.sp)
        Spacer(Modifier.height(16.dp))

        Text("Server Name", color = V380Colors.textMuted, fontSize = 11.sp)
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(V380Colors.surfaceAlt, RoundedCornerShape(4.dp))
                .border(1.dp, V380Colors.border, RoundedCornerShape(4.dp))
        )
        Spacer(Modifier.height(12.dp))

        Text("IP Address", color = V380Colors.textMuted, fontSize = 11.sp)
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(V380Colors.surfaceAlt, RoundedCornerShape(4.dp))
                .border(1.dp, V380Colors.border, RoundedCornerShape(4.dp))
        )
        Spacer(Modifier.height(12.dp))

        Text("Port", color = V380Colors.textMuted, fontSize = 11.sp)
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(40.dp)
                .background(V380Colors.surfaceAlt, RoundedCornerShape(4.dp))
                .border(1.dp, V380Colors.border, RoundedCornerShape(4.dp))
        )
        Spacer(Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .clickable { onDismiss() }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text("Cancel", color = V380Colors.textMuted, fontSize = 13.sp)
            }
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(V380Colors.accent, RoundedCornerShape(4.dp))
                    .clickable {
                        if (name.isNotBlank() && ip.isNotBlank()) {
                            scope.launch {
                                repository.addServer(name, ip, port.toIntOrNull() ?: 8080)
                                onSave()
                            }
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text("Connect", color = V380Colors.text, fontSize = 13.sp)
            }
        }
    }
}