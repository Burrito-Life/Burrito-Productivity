package com.burrito.productivity.ai.prompt

import com.burrito.productivity.data.local.AppDatabase

/**
 * Represents a user persona configuration.
 * TODO: Refactor this to a more robust data model
 */
data class UserPersona(
    val settings: Map<String, Any> = emptyMap()
)

/**
 * Represents an AI persona configuration.
 * TODO: Refactor this to a more robust data model
 */
data class AIPersona(
    val settings: Map<String, Any> = emptyMap()
)

/**
 * Manages the active user persona and settings.
 * Handles "Configuration as Data" for the profile of the user with regard to how they want to receive information.
 * TODO: These persona representations should be put into the system prompt in as dense a format as possible, and any computational expense for condensing that should be here rather than in the SystemPromptHydrator.kt file, because that will be run an immense amount of times.
 */
class PersonaManager(
    private val appDb: AppDatabase
) {
    suspend fun getActiveUserPersona(): UserPersona {
        val currentUserPersona = appDb.getUserPersona()
        val currentUserPersonaSettings = currentUserPersona.settings.toMutableMap()
        return UserPersona(currentUserPersonaSettings.toMap())
    }

    suspend fun updateUserPersona(key: String, value: Any) {
        val currentUserPersona = appDb.getUserPersona()
        val currentUserPersonaSettings = currentUserPersona.settings.toMutableMap()
        currentUserPersonaSettings[key] = value
        appDb.setUserPersonaSettings(currentUserPersonaSettings)
    }

    suspend fun getActiveAIPersona(): AIPersona {
        val currentAIPersona = appDb.getAIPersona()
        val currentAIPersonaSettings = currentAIPersona.settings.toMutableMap()
        return AIPersona(currentAIPersonaSettings.toMap())
    }

    suspend fun updateAIPersona(key: String, value: Any) {
        val currentAIPersona = appDb.getAIPersona()
        val currentAIPersonaSettings = currentAIPersona.settings.toMutableMap()
        currentAIPersonaSettings[key] = value
        appDb.setAIPersonaSettings(currentAIPersonaSettings)
    }
}