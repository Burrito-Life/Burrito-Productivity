package com.burrito.productivity.ai.orchestration

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.burrito.productivity.ai.agents.ReasonerAgent
import com.burrito.productivity.ai.agents.CriticAgent
import com.burrito.productivity.ai.agents.TriageAgent
import com.burrito.productivity.ai.agents.AgentTeam
import com.burrito.productivity.ai.prompt.PersonaManager
import com.burrito.productivity.ai.prompt.SystemPromptHydrator
import com.burrito.productivity.ui.BurritoUI

/**
 * The Hub. Orchestrates the flow of data between the UI, the Brain (Agents), and Data.
 * Manages the AG-UI event stream.
 */
class AgentOrchestrator(
    private val reasoner: ReasonerAgent,
    private val critic: CriticAgent,
    private val triage: TriageAgent,
    private val personaManager: PersonaManager,
    private val promptHydrator: SystemPromptHydrator
) {
    
    private val agentTeam = AgentTeam(triage, reasoner, critic)

    // Placeholder for A2UI event structure
    data class UIEvent(
        val type: String,
        val payload: String,
        val state: InteractionState
    )

    fun process(input: String): Flow<UIEvent> = flow {
        // 1. Triage
        emit(UIEvent("status", "Triaging Request...", InteractionState.Thinking))
        val triagedRequest = agentTeam.triage(input)

        // 2. Respond
        emit(UIEvent("status", "Generating Response...", InteractionState.Thinking))
        val initialResponse = agentTeam.respond(triagedRequest)

        // 3. Critique
        emit(UIEvent("status", "Critiquing Response...", InteractionState.Thinking))
        val critique = agentTeam.critique(initialResponse)

        // 4. Revise
        emit(UIEvent("status", "Revising...", InteractionState.Thinking))
        val revisedResponse = agentTeam.revise(initialResponse, critique)

        // 5. UI Build
        val responseUIRepresentation = BurritoUI.buildUIRepresentationForAIResponse(revisedResponse)

        when (responseUIRepresentation) {
            is BurritoUI.UIRepresentation.Text -> {
                emit(UIEvent("response", responseUIRepresentation.content, InteractionState.Idle))
            }
            is BurritoUI.UIRepresentation.Component -> {
                // In a real app, payload would be the serialized component data
                emit(UIEvent("component", "Dynamic Component Rendered", InteractionState.Idle))
            }
            is BurritoUI.UIRepresentation.Error -> {
                emit(UIEvent("error", responseUIRepresentation.message, InteractionState.Idle))
            }
        }
    }
}
