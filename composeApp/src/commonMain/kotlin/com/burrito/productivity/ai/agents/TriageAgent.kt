package com.burrito.productivity.ai.agents

/**
 * Agent responsible for analyzing the user's input and deciding which capability/model is best suited for the job.
 */
class TriageAgent {
    data class TriagedRequest(
        val originalInput: String,
        val intent: String,
        val recommendedModel: String,
        val priority: String
    )

    suspend fun triage(userInput: String): TriagedRequest {
        // Placeholder logic: specific model selection would happen here
        return TriagedRequest(
            originalInput = userInput,
            intent = "general_query",
            recommendedModel = "gemini-pro",
            priority = "normal"
        )
    }
}
