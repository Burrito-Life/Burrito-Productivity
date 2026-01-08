package com.burrito.productivity.ai.prompt

import com.burrito.productivity.ai.context.SemanticContext

/**
 * Compiles the system prompt by "hydrating" templates with Persona settings and Semantic Context.
 * TODO: Consider having this as just a floating util function rather than a class (perhaps in a file that collects util functions), so we can skip dependency injection in class instantiation
 */
class SystemPromptHydrator {
    fun compile(persona: UserPersona, context: SemanticContext): String {
        // Placeholder for template engine logic
        return """
            You are a helpful assistant.
            Settings: ${persona.settings}
            Context: ${context.activeTask}
        """.trimIndent() // TODO: For the immense amount of times this will be run, I think we should reformat our string so that we skip calling trimIndent() - it will add up in a multi-AI-call response to a user input
    }
}
