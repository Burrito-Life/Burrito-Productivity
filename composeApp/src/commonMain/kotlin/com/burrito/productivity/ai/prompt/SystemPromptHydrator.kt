package com.burrito.productivity.ai.prompt

import com.burrito.productivity.ai.context.SemanticContext

/**
 * Compiles the system prompt by "hydrating" templates with Persona settings and Semantic Context.
 */
class SystemPromptHydrator {
    fun compile(persona: UserPersona, context: SemanticContext): String {
        // Placeholder for template engine logic
        return """
            You are a helpful assistant.
            Settings: ${persona.settings}
            Context: ${context.activeTask}
        """.trimIndent()
    }
}
