package com.v380.client.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.v380.client.data.model.Server
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "v380_settings")

class ServerRepository(private val context: Context) {

    private val serverCountKey = intPreferencesKey("server_count")
    private fun serverNameKey(id: Long) = stringPreferencesKey("server_${id}_name")
    private fun serverIpKey(id: Long) = stringPreferencesKey("server_${id}_ip")
    private fun serverPortKey(id: Long) = intPreferencesKey("server_${id}_port")

    val servers: Flow<List<Server>> = context.dataStore.data.map { prefs ->
        val count = prefs[serverCountKey] ?: 0
        (0 until count).map { i ->
            Server(
                id = i.toLong(),
                name = prefs[serverNameKey(i.toLong())] ?: "Camera ${i + 1}",
                ip = prefs[serverIpKey(i.toLong())] ?: "",
                port = prefs[serverPortKey(i.toLong())] ?: 8080,
            )
        }
    }

    suspend fun addServer(name: String, ip: String, port: Int = 8080) {
        context.dataStore.edit { prefs ->
            val count = prefs[serverCountKey] ?: 0
            val id = count.toLong()
            prefs[serverNameKey(id)] = name
            prefs[serverIpKey(id)] = ip
            prefs[serverPortKey(id)] = port
            prefs[serverCountKey] = count + 1
        }
    }

    suspend fun deleteServer(id: Long) {
        context.dataStore.edit { prefs ->
            prefs.remove(serverNameKey(id))
            prefs.remove(serverIpKey(id))
            prefs.remove(serverPortKey(id))
        }
    }
}