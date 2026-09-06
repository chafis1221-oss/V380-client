package com.v380.client.data.model

data class Server(
    val id: Long,
    val name: String,
    val ip: String,
    val port: Int = 8080,
    val isActive: Boolean = false,
    val fps: Int = 0,
    val resolution: String = "",
    val quality: String = "",
)

data class CameraStatus(
    val status: String = "",
    val timestamp: String = "",
)

data class ApiResponse(
    val status: String = "",
    val cmd: String = "",
)

enum class PtzDirection { UP, DOWN, LEFT, RIGHT, STOP }
enum class LightMode { ON, OFF, AUTO }
enum class ImageMode { COLOR, BW, AUTO, FLIP }