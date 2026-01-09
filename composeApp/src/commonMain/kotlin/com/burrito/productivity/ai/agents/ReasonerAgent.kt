package com.burrito.productivity.ai.agents

import com.burrito.productivity.data.mcp.MCPToolRegistry
import com.burrito.productivity.data.local.AppDatabase
import com.burrito.productivity.ai.agents.ReasoningResponse
import com.burrito.productivity.ai.agents.CritiqueResponse
import com.burrito.productivity.ai.agents.ModelWrapper
import com.burrito.productivity.ai.prompt.PromptUtils
import com.burrito.productivity.ai.context.SemanticContext

/**
 * The primary agent that "thinks" about and responds to the user's input (supported by other agents).
 * It reasons about the user's input and utilizes available MCP tools and RAG context to formulate a response.
 * TODO: Clean up this placeholder implementation to make sure it's properly using the model, tools, and context.
 */
class ReasonerAgent(
    private val toolRegistry: MCPToolRegistry,
    private val appDb: AppDatabase,
    private val modelWrapper: ModelWrapper
) {
    suspend fun solve(userInput: String, ragContext: RAGContext? = null, semanticContext: SemanticContext? = null): ReasoningResponse {
        return modelWrapper.invoke(userInput, parameters = mapOf(
            "tools" to toolRegistry.getAllTools(),
            "ragContext" to (PromptUtils.getStringOfRAGContext(ragContext)),
            "semanticContext" to (PromptUtils.getStringOfSemanticContext(semanticContext))
        ))
    }

    suspend fun revise(
        previousResponse: ReasoningResponse,
        critique: CritiqueResponse,
        ragContext: RAGContext? = null,
        semanticContext: SemanticContext? = null
    ): ReasoningResponse {
        // TODO: Real implementation that uses LLMs, MCP tools, and RAG context
        return modelWrapper.invoke(
            prompt = PromptUtils.systemPromptHydrator(appDb.getUserPersona(), ragContext, semanticContext),
            parameters = mapOf(
                "previousResponse" to previousResponse,
                "critique" to critique,
                "tools" to toolRegistry.getAllTools(),
                "ragContext" to (PromptUtils.getStringOfRAGContext(ragContext)),
                "semanticContext" to (PromptUtils.getStringOfSemanticContext(semanticContext))
            )
        )
    }
}