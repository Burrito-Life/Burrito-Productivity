package com.burrito.productivity.ai.orchestration

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.burrito.productivity.ai.agents.ReasonerAgent
import com.burrito.productivity.ai.agents.CriticAgent
import com.burrito.productivity.ai.agents.TriageAgent
import com.burrito.productivity.ai.agents.AgentTeam
import com.burrito.productivity.ai.agents.WorkflowStep
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
        // 1. Triage: Get the plan
        emit(UIEvent("status", "Triaging Request...", InteractionState.Thinking))
        val triagedRequest = agentTeam.triage(input)
        
        // State variables to hold data between steps
        var currentResponse: AgentTeam.InitialResponse? = null
        var currentCritique: AgentTeam.Critique? = null
        var finalRevised: AgentTeam.RevisedResponse? = null

        // 2. Execute Dynamic Workflow
        for (step in triagedRequest.steps) {
            when (step) {
                is WorkflowStep.Reason -> {
                    emit(UIEvent("status", "Reasoning...", InteractionState.Thinking))
                    // If we haven't reasoned yet, do it. 
                    // If we have a context override (e.g. from previous steps?), use it.
                    currentResponse = agentTeam.respond(triagedRequest, step.contextOverride)
                }
                is WorkflowStep.Critique -> {
                    if (currentResponse != null) {
                        emit(UIEvent("status", "Critiquing...", InteractionState.Thinking))
                        currentCritique = agentTeam.critique(currentResponse!!, step.criteria)
                    } else {
                        emit(UIEvent("error", "Cannot critique: no response generated yet", InteractionState.Idle))
                    }
                }
                is WorkflowStep.Revise -> {
                    if (currentResponse != null && currentCritique != null) {
                        emit(UIEvent("status", "Revising...", InteractionState.Thinking))
                        finalRevised = agentTeam.revise(currentResponse!!, currentCritique!!)
                    } else {
                         emit(UIEvent("error", "Cannot revise: missing response or critique", InteractionState.Idle))
                    }
                }
                is WorkflowStep.GenerateUI -> {
                    emit(UIEvent("status", "Building UI...", InteractionState.Thinking))
                    // If we have a revised response, use it. Otherwise, fallback to initial response content if available
                    val responseToRender = finalRevised ?: currentResponse?.let { 
                        AgentTeam.RevisedResponse(it.content) 
                    }
                    
                    if (responseToRender != null) {
                         val uiRep = BurritoUI.buildUIRepresentationForAIResponse(responseToRender)
                         // Emit the UI event based on the representation
                         when (uiRep) {
                            is BurritoUI.UIRepresentation.Text -> 
                                emit(UIEvent("response", uiRep.content, InteractionState.Idle))
                            is BurritoUI.UIRepresentation.Component -> 
                                emit(UIEvent("component", "Dynamic Component (${step.style})", InteractionState.Idle))
                            is BurritoUI.UIRepresentation.Error -> 
                                emit(UIEvent("error", uiRep.message, InteractionState.Idle))
                         }
                    } else {
                         emit(UIEvent("error", "Nothing to render", InteractionState.Idle))
                    }
                }
                is WorkflowStep.ExecuteTool -> {
                    emit(UIEvent("status", "Executing Tool: ${step.toolName}...", InteractionState.ExecutingTask))
                    // Placeholder for tool execution
                }
            }
        }
    }
}
