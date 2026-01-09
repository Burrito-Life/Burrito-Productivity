package com.burrito.productivity.data.local

import com.burrito.productivity.data.local.DbServiceImpl

/**
 * Interface for managing Core Settings in memory and in the local database.
 */
interface CoreSettings {
    suspend fun getCoreSettings(): Map<String, Any>
    suspend fun setCoreSettings(settings: Map<String, Any>): Int
}

class CoreSettingsImpl(
    private val dbService: DbServiceImpl
) : CoreSettings {
    override suspend fun getCoreSettings(): Map<String, Any> {
        // TODO: Real query to replace placeholder
        val query = "SELECT * FROM core_settings"
        val dbResults = dbService.executeQuery(query)
        return mapOf("results" to dbResults) // TODO: Parse results properly
    }

    override suspend fun setCoreSettings(settings: Map<String, Any>): Int {
        // TODO: Real query to replace placeholder
        val query = "UPDATE core_settings SET settings = ?"
        return dbService.executeUpdate(query)
    }
}