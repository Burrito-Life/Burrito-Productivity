package com.burrito.productivity.ai.agents

/**
 * Defines the atomic steps that can be composed into a workflow.
 */
sealed interface WorkflowStep {
    data class Reason(val contextOverride: String? = null) : WorkflowStep
    data class Critique(val criteria: String = "standard") : WorkflowStep
    data object Revise : WorkflowStep
    data class GenerateUI(val style: String) : WorkflowStep // e.g., "chat", "canvas", "onboarding"
    data class ExecuteTool(val toolName: String, val args: Map<String, Any>) : WorkflowStep
}

/**
 * Agent responsible for analyzing the user's input and deciding which capability/model is best suited for the job.
 * Now returns a dynamic workflow plan.
 */
class TriageAgent {
    data class TriagedRequest(
        val originalInput: String,
        val steps: List<WorkflowStep>,
        val estimatedComplexity: String = "medium"
    )

    suspend fun triage(userInput: String): TriagedRequest {
        // In a real implementation, this logic would be driven by an LLM or rules engine
        // to detect intents like "draw a diagram" vs "explain this code".
        
        val steps = mutableListOf<WorkflowStep>()
        
        // Default "Smart Chat" Flow
        steps.add(WorkflowStep.Reason())
        steps.add(WorkflowStep.Critique())
        steps.add(WorkflowStep.Revise)
        steps.add(WorkflowStep.GenerateUI("chat_response"))
        
        return TriagedRequest(
            originalInput = userInput,
            steps = steps
        )
    }
}
