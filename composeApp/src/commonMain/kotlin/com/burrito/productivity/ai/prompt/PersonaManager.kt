package com.burrito.productivity.ai.prompt

/**
 * Represents a user persona configuration.
 */
data class UserPersona(
    val settings: Map<String, Any> = emptyMap()
)

/**
 * Manages the active user persona and settings.
 * Handles "Configuration as Data" for the profile of the user with regard to how they want to receive information.
 * TODO: We should use not only a UserPersona but also an AIPersona. These persona representations should be put into the system prompt in as dense a format as possible, and any computational expense for condensing that should be here rather than in the SystemPromptHydrator.kt file, because that will be run an immense amount of times.
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
