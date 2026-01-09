package com.burrito.productivity.ai.orchestration

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.burrito.productivity.ai.agents.ReasoningResponse
import com.burrito.productivity.ai.agents.CritiqueResponse
import com.burrito.productivity.ai.agents.RevisedResponse
import com.burrito.productivity.ai.agents.ReasonerAgent
import com.burrito.productivity.ai.agents.CriticAgent
import com.burrito.productivity.ai.agents.TriageAgent
import com.burrito.productivity.ai.agents.WorkflowStep
import com.burrito.productivity.ai.prompt.PersonaManager
import com.burrito.productivity.ai.prompt.PromptUtils
import com.burrito.productivity.ui.BurritoUI
import com.burrito.productivity.ai.context.SemanticContext

/**
 * The Hub. Orchestrates the flow of data between the UI, the Brain (Agents), and Data.
 * Manages the AG-UI event stream.
 */
class AgentOrchestrator(
    private val personaManager: PersonaManager,
    private val promptHydrator: (persona: UserPersona, context: SemanticContext) -> String,
    private val triageAgent: TriageAgent,
    private val reasoner: ReasonerAgent,
    private val critic: CriticAgent
) {
    // Placeholder for A2UI event structure
    data class UIEvent(
        val type: String,
        val payload: String,
        val state: InteractionState
    )

    fun process(input: String): Flow<UIEvent> = flow {
        // 1. Triage: Get the plan
        emit(UIEvent("status", "Triaging Request...", InteractionState.Thinking))
        val triagedRequest = triageAgent.triage(input)
        // TODO: If userSettings
        
        // State variables to hold data between steps
        val reasoningResponses = mutableListOf<ReasoningResponse>()
        val criticResponses = mutableListOf<CritiqueResponse>()
        var finalRevised: RevisedResponse? = null
        
        // TODO: Get actual semantic context
        val semanticContext = SemanticContext(activeTask = "User Input: $input")
        val systemPrompt = promptHydrator.compile(personaManager.getActivePersona(), semanticContext)

        // 2. Execute Dynamic Workflow
        val flowSteps = triagedRequest.steps
        for (step in flowSteps) {
            when (step) {
                is WorkflowStep.Reason -> {
                    emit(UIEvent("status", "Thinking...", InteractionState.Thinking))
                    // If we have a context override (e.g. from previous steps?), use it.
                    val stepInput = step.contextOverride ?: input
                    val response = reasoner.solve(stepInput, systemPrompt)
                    reasoningResponses.add(response)
                }
                is WorkflowStep.Critique -> {
                    val lastResponse = reasoningResponses.lastOrNull()
                    if (lastResponse != null) {
                        emit(UIEvent("status", "Reviewing Response...", InteractionState.Thinking))
                        val critique = critic.critique(lastResponse, step.criteria)
                        criticResponses.add(critique)
                        // TODO: Implement the ability for the CriticAgent to flag `reCritiqueRequired` for larger issues that need another review
                    } else {
                        emit(UIEvent("error", "Cannot critique: no response generated yet", InteractionState.Idle))
                    }
                }
                is WorkflowStep.Revise -> {
                    val currentResponse = reasoningResponses.lastOrNull()
                    val currentCritique = criticResponses.lastOrNull()
                    
                    if (currentResponse != null && currentCritique != null) {
                        emit(UIEvent("status", "Revising Response...", InteractionState.Thinking))
                        val revised = reasoner.revise(currentResponse, currentCritique, systemPrompt)
                        reasoningResponses.add(revised) // Add revised response as the new latest response
                        
                        // Also update finalRevised holder
                        finalRevised = RevisedResponse(revised.text, revised.a2ui)
                    } else {
                        emit(UIEvent("error", "Cannot revise: missing response or critique", InteractionState.Idle))
                    }
                }
                is WorkflowStep.GenerateUI -> {
                    emit(UIEvent("status", "Building UI...", InteractionState.Thinking))
                    // If we have a revised response, use it. Otherwise, fallback to initial response content if available
                    val responseToRender = finalRevised ?: reasoningResponses.lastOrNull()?.let { 
                        RevisedResponse(it.text, it.a2ui)
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