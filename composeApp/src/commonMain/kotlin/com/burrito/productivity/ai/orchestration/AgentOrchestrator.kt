package com.burrito.productivity.ai.orchestration

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.burrito.productivity.ai.agents.Types.ReasoningResponse
import com.burrito.productivity.ai.agents.Types.CritiqueResponse
import com.burrito.productivity.ai.agents.Types.RAGContext
import com.burrito.productivity.ai.agents.ReasonerAgent
import com.burrito.productivity.ai.agents.CriticAgent
import com.burrito.productivity.ai.agents.TriageAgent
import com.burrito.productivity.ai.agents.AgentOrchestrator
import com.burrito.productivity.ai.agents.WorkflowStep
import com.burrito.productivity.ai.prompt.PersonaManager
import com.burrito.productivity.ai.prompt.SystemPromptHydrator
import com.burrito.productivity.ui.BurritoUI

/**
 * The Hub. Orchestrates the flow of data between the UI, the Brain (Agents), and Data.
 * Manages the AG-UI event stream.
 * TODO: Implement the use of SystemPromptHydrator. Make sure to make it flexible and as "intelligent" as possible.
 */
class AgentOrchestrator(
    private val personaManager: PersonaManager,
    private val promptHydrator: SystemPromptHydrator,
    private val triageAgent: TriageAgent,
    private val reasoner: ReasonerAgent,
    private val critic: CriticAgent
) {
    // Placeholder for A2UI event structure
    // TODO: Use the proper A2UI structure
    data class UIEvent(
        val type: String,
        val payload: String,
        val state: InteractionState
    )

    fun process(input: String): Flow<UIEvent> = flow {
        // 1. Triage: Get the plan
        emit(UIEvent("status", "Triaging Request...", InteractionState.Thinking))
        val triagedRequest = triageAgent.triage(input)
        
        // State variables to hold data between steps
        var reasoningResponses: ReasoningResponse[] = []
        var criticResponses: CritiqueResponse[] = []

        // 2. Execute Dynamic Workflow
        val flowSteps = triagedRequest.steps
        for (step in flowSteps) {
            when (step) {
                is WorkflowStep.Reason -> {
                    emit(UIEvent("status", "Thinking...", InteractionState.Thinking))
                    // If we have a context override (e.g. from previous steps?), use it.
                    val input = contextOverride ?: request.originalInput
                    val response = reasonerAgent.solve(input)
                    reasoningResponses.add(reasoner.solve(triagedRequest, step.contextOverride))
                }
                is WorkflowStep.CritiqueResponse -> {
                    if (reasoningResponses.last() != null) {
                        emit(UIEvent("status", "Reviewing Response...", InteractionState.Thinking))
                        critiqueResponses.add(critic.critique(reasoningResponses.last()!!, step.criteria))
                        // TODO: Implement the ability for the CriticAgent to flag `reCritiqueRequired` for larger issues that need another review, which then gets checked here to add onto `flowSteps` - IMPORTANT: Prevent infinite loop, either with a hard limit or some smarter approach
                    } else {
                        emit(UIEvent("error", "Cannot critique: no response generated yet", InteractionState.Idle))
                    }
                }
                is WorkflowStep.Revise -> {
                    if (currentResponse != null && currentCritique != null) {
                        emit(UIEvent("status", "Revising Response...", InteractionState.Thinking))
                        reasoningResponses.add(reasoner.revise(currentResponse!!, currentCritique!!))
                    } else {
                        emit(UIEvent("error", "Cannot revise: missing response or critique", InteractionState.Idle))
                    }
                }
                is WorkflowStep.GenerateUI -> {
                    emit(UIEvent("status", "Building UI...", InteractionState.Thinking))
                    // If we have a revised response, use it. Otherwise, fallback to initial response content if available
                    val responseToRender = finalRevised ?: currentResponse?.let { 
                        AgentOrchestrator.RevisedResponse(it.content) 
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
