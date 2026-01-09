package com.burrito.productivity.data.local

import com.burrito.productivity.data.local.DbServiceImpl

/**
 * Interface for managing User Persona Settings in memory and in the local database.
 */
interface UserPersonaSettings {
    suspend fun getUserPersonaSettings(): Map<String, Any>
    suspend fun setUserPersonaSettings(settings: Map<String, Any>): Int
}

class UserPersonaSettingsImpl(
    private val dbService: DbServiceImpl
) : UserPersonaSettings {
    override suspend fun getUserPersonaSettings(): Map<String, Any> {
        // TODO: Real query to replace placeholder
        val query = "SELECT * FROM user_persona_settings"
        val dbResults = dbService.executeQuery(query)
        return mapOf("results" to dbResults) // TODO: Parse results properly
    }
    override suspend fun setUserPersonaSettings(settings: Map<String, Any>): Int {
        // TODO: Real query to replace placeholder
        val query = "UPDATE user_persona_settings SET settings = ?"
        return dbService.executeUpdate(query)
    }
}