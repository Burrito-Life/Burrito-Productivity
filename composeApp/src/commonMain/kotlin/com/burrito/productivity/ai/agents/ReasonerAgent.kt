package com.burrito.productivity.ai.agents

import com.burrito.productivity.data.mcp.McpToolRegistry

/**
 * The primary agent that decides "what to do".
 * It reasons about the user's input and utilizes available MCP tools to formulate a response.
 * TODO: Implement real model calls with MCP tools and RAG context (passed in through dependency injection)
 */
class ReasonerAgent implements WorkflowStep.Reason (
    private val toolRegistry: McpToolRegistry
) {
    data class AgentResponse(
        val content: String,
        val toolUsage: List<String> = emptyList()
    )

    suspend fun solve(userInput: String): ReasoningResponse {
        // Placeholder logic for reasoning
        // In a real implementation, this would call an LLM with the available tool signatures
        return ReasoningResponse(
            text = "I have reasoned about: $userInput",
            a2ui = mapOf(),
            metadata = mapOf()
        )
    }
}
