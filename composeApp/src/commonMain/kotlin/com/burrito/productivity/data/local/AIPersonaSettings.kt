package com.burrito.productivity.data.local

import com.burrito.productivity.data.local.DbServiceImpl

/**
 * Interface for managing AI Persona Settings in memory and in the local database.
 */
interface AIPersonaSettings {
    suspend fun loadAIPersonaSettings(): Map<String, Any>
    suspend fun setAIPersonaSettings(settings: Map<String, Any>): Int
}

class AIPersonaSettingsImpl(
    private val dbService: DbServiceImpl
) : AIPersonaSettings {
    private var currentAIPersonaSettings: Map<String, Any>? = null
    override suspend fun loadAIPersonaSettings(): Map<String, Any> {
        // TODO: Real query to replace placeholder
        val query = "SELECT * FROM ai_persona_settings"
        val dbResults = dbService.executeQuery(query)
        return mapOf("results" to dbResults) // TODO: Parse results properly
    }
    override suspend fun setAIPersonaSettings(settings: Map<String, Any>): Int {
        this.currentAIPersonaSettings = settings
        // TODO: Real query to replace placeholder
        val query = "UPDATE ai_persona_settings SET settings = ?"
        return dbService.executeUpdate(query)
    }
}