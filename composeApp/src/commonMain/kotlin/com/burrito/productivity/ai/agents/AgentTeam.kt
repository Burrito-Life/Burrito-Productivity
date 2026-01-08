package com.burrito.productivity.ai.agents

import com.burrito.productivity.ai.agents.TriageAgent.TriagedRequest

/**
 * Manages the collaboration between different agents (Triage, Reasoner, Critic)
 * to produce a high-quality response.
 */
class AgentTeam(
    private val triageAgent: TriageAgent,
    private val reasonerAgent: ReasonerAgent,
    private val criticAgent: CriticAgent
) {

    // Rich data objects for the workflow
    data class InitialResponse(
        val triagedRequest: TriagedRequest,
        val content: String
    )

    data class Critique(
        val passed: Boolean,
        val feedback: String,
        val score: Int
    )

    data class RevisedResponse(
        val finalContent: String,
        val a2ui: Map<String, Any>? = null, // A2UI data for UI hydration
        val metadata: Map<String, Any> = emptyMap()
    )

    suspend fun triage(userInput: String): TriagedRequest {
        return triageAgent.triage(userInput)
    }

    suspend fun respond(request: TriagedRequest, contextOverride: String? = null): InitialResponse {
        // The reasoner uses the triaged info to solve the problem
        val input = contextOverride ?: request.originalInput
        val response = reasonerAgent.solve(input)
        return InitialResponse(
            triagedRequest = request,
            content = response.content
        )
    }

    suspend fun critique(response: InitialResponse, criteria: String = "standard"): Critique {
        val validation = criticAgent.validate(CriticAgent.Proposal(response.content))
        return Critique(
            passed = validation.isValid,
            feedback = validation.feedback,
            score = if (validation.isValid) 10 else 5
        )
    }

    suspend fun revise(response: InitialResponse, critique: Critique): RevisedResponse {
        return if (critique.passed) {
            RevisedResponse(finalContent = response.content)
        } else {
            // In a real flow, we would feed the critique back into the Reasoner
            val improvedContent = "${response.content} (Revised based on: ${critique.feedback})"
            RevisedResponse(finalContent = improvedContent)
        }
    }
}
