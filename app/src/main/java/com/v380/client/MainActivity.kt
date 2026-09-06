package com.v380.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.v380.client.data.repository.ServerRepository
import com.v380.client.ui.navigation.V380NavGraph
import com.v380.client.ui.theme.V380Theme

class MainActivity : ComponentActivity() {
    private lateinit var repository: ServerRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = ServerRepository(applicationContext)

        setContent {
            V380Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val servers = repository.servers.collectAsState(initial = emptyList()).value
                    V380NavGraph(
                        navController = navController,
                        repository = repository,
                        servers = servers,
                    )
                }
            }
        }
    }
}