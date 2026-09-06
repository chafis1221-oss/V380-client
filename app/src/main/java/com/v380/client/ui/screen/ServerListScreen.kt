package com.v380.client.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v380.client.data.model.Server
import com.v380.client.data.repository.ServerRepository
import com.v380.client.ui.component.ServerCard
import com.v380.client.ui.theme.V380Colors
import kotlinx.coroutines.launch

@Composable
fun ServerListScreen(
    repository: ServerRepository,
    onServerClick: (Server) -> Unit,
    onAddClick: () -> Unit,
) {
    val servers by repository.servers.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(V380Colors.bg)
            .padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        ) {
            Text(
                text = "Servers",
                color = V380Colors.text,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .background(V380Colors.accent, RoundedCornerShape(4.dp))
                    .border(1.dp, V380Colors.accent, RoundedCornerShape(4.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .let { mod ->
                        mod
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "+ Add",
                    color = V380Colors.text,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .let { Text(it) },
                )
            }
        }

        if (servers.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No servers", color = V380Colors.textMuted, fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("Tap + Add to add your V380 bridge", color = V380Colors.textDim, fontSize = 12.sp)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(servers) { server ->
                    ServerCard(
                        server = server,
                        onClick = { onServerClick(server) },
                        onDelete = { },
                    )
                }
            }
        }
    }
}