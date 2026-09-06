package com.v380.client.data.network

import com.v380.client.data.model.ApiResponse
import com.v380.client.data.model.CameraStatus
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class CameraApi(private val baseUrl: String) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    private fun url(path: String) = "$baseUrl$path"

    suspend fun ptz(direction: String): Result<ApiResponse> = post("/api/ptz/$direction")
    suspend fun light(mode: String): Result<ApiResponse> = post("/api/light/$mode")
    suspend fun image(mode: String): Result<ApiResponse> = post("/api/image/$mode")
    suspend fun getStatus(): Result<CameraStatus> = get("/api/status")

    private suspend fun post(path: String): Result<ApiResponse> = runCatching {
        val request = Request.Builder().url(url(path)).post(okhttp3.RequestBody.create(null, "")).build()
        val response = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            client.newCall(request).execute()
        }
        if (!response.isSuccessful) throw Exception("HTTP ${response.code}")
        val body = response.body?.string() ?: throw Exception("Empty response")
        com.v380.client.data.model.ApiResponse()
    }

    private suspend fun get(path: String): Result<CameraStatus> = runCatching {
        val request = Request.Builder().url(url(path)).get().build()
        val response = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            client.newCall(request).execute()
        }
        if (!response.isSuccessful) throw Exception("HTTP ${response.code}")
        CameraStatus(status = "running")
    }

    fun buildStreamUrl(): String = "$baseUrl/stream.h265"
}