package com.burrito.productivity.ai.prompt

import com.burrito.productivity.ai.context.SemanticContext
import com.burrito.productivity.ai.agents.RAGContext

object PromptUtils {
    /**
    * Compiles the system prompt by "hydrating" templates with Persona settings and Semantic Context.
    */
    fun systemPromptHydrator(persona: UserPersona, ragContext: RAGContext? = null, semanticContext: SemanticContext? = null): String {
        // TODO: Make this function as flexible and as "intelligent" as possible, because this is crucial to how "smart" our app feels
        // NOTE: KEEP THIS AS A SINGLE LINE STRING RETURN FOR MAXIMAL EFFICIENCY, because this will be called a LOT
        return "You are a helpful assistant.\nSettings: ${PromptUtils.getStringOfUserPersonaSettings(persona)}\n${PromptUtils.getStringOfRAGContext(ragContext)}\n${PromptUtils.getStringOfSemanticContext(semanticContext)}"
    }

    fun getStringOfUserPersonaSettings(persona: UserPersona): String {
        // TODO: This will need to be something more robust, but we must keep it maximally efficient (because it will be called a LOT)
        return persona.settings.entries.joinToString(separator = ", ") { "${it.key}: ${it.value}" }
    }

    fun getStringOfRAGContext(context: RAGContext? = null): String {
        // TODO: This will need to be something more robust, but we must keep it maximally efficient (because it will be called a LOT)
        context?.let { return "RAG Context Data: ${it}" }
        return ""
    }

    fun getStringOfSemanticContext(context: SemanticContext? = null): String {
        // TODO: This will need to be something more robust, but we must keep it maximally efficient (because it will be called a LOT)
        // return "Active Task: ${context?.activeTask}"
        context?.activeTask?.let { return "RAG Context Data: ${it}" }
        return ""
    }

    fun getStringOfAIPersonaSettings(aiPersona: AIPersona): String {
        // TODO: This will need to be something more robust, but we must keep it maximally efficient (because it will be called a LOT)
        return aiPersona.settings.entries.joinToString(separator = ", ") { "${it.key}: ${it.value}" }
    }
}