package com.v380.client.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.v380.client.data.model.Server
import com.v380.client.data.repository.ServerRepository
import com.v380.client.ui.screen.AddServerDialog
import com.v380.client.ui.screen.CameraViewerScreen
import com.v380.client.ui.screen.ServerListScreen

object Routes {
    const val SERVER_LIST = "server_list"
    const val CAMERA_VIEWER = "camera_viewer/{serverId}"
    const val ADD_SERVER = "add_server"

    fun cameraViewer(serverId: Long) = "camera_viewer/$serverId"
}

@Composable
fun V380NavGraph(
    navController: NavHostController,
    repository: ServerRepository,
    servers: List<Server>,
) {
    NavHost(navController = navController, startDestination = Routes.SERVER_LIST) {
        composable(Routes.SERVER_LIST) {
            ServerListScreen(
                repository = repository,
                onServerClick = { server ->
                    navController.navigate(Routes.cameraViewer(server.id))
                },
                onAddClick = {
                    navController.navigate(Routes.ADD_SERVER)
                },
            )
        }
        composable(Routes.CAMERA_VIEWER) { backStackEntry ->
            val serverId = backStackEntry.arguments?.getString("serverId")?.toLongOrNull() ?: return@composable
            val server = servers.find { it.id == serverId } ?: return@composable
            CameraViewerScreen(
                server = server,
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.ADD_SERVER) {
            AddServerDialog(
                onDismiss = { navController.popBackStack() },
                onSave = { navController.popBackStack() },
                repository = repository,
            )
        }
    }
}