package com.burrito.productivity.ai.prompt

/**
 * Represents a user persona configuration.
 */
data class UserPersona(
    val settings: Map<String, Any> = emptyMap()
)

/**
 * Manages the active user persona and settings.
 * Handles "Configuration as Data".
 */
class PersonaManager {
    private val currentSettings = mutableMapOf<String, Any>()

    fun getActivePersona(): UserPersona {
        return UserPersona(currentSettings.toMap())
    }

    fun updatePersona(key: String, value: Any) {
        currentSettings[key] = value
    }
}
