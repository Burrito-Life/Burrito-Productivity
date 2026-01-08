package com.burrito.productivity.ai.agents

import com.burrito.productivity.data.mcp.McpToolRegistry

/**
 * The primary agent that decides "what to do".
 * It reasons about the user's input and utilizes available MCP tools to formulate a response.
 */
class ReasonerAgent(
    private val toolRegistry: McpToolRegistry
) {
    data class AgentResponse(
        val content: String,
        val toolUsage: List<String> = emptyList()
    )

    suspend fun solve(userInput: String): AgentResponse {
        // Placeholder logic for reasoning
        // In a real implementation, this would call an LLM with the available tool signatures
        return AgentResponse(
            content = "I have reasoned about: $userInput",
            toolUsage = emptyList()
        )
    }
}
