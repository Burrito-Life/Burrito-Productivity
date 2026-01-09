package com.burrito.productivity.ai.agents

/**
 * Defines the atomic steps that can be composed into a workflow.
 * TODO: Shouldn't `Revise` be a function like the others? I'm pretty sure we'll need it to be a function...
 * TODO: `contextOverride`, `criteria`, `toolName`, and `style` should be standardized via an enum or similar for similar safety as we get here for WorkflowStep options
 */
sealed interface WorkflowStep {
    data class Reason(val contextOverride: String? = null) : WorkflowStep
    data class Critique(val criteria: String = "standard") : WorkflowStep
    data object Revise : WorkflowStep
    data class GenerateUI(val style: String) : WorkflowStep // e.g., "chat", "canvas", "onboarding"
    data class ExecuteTool(val toolName: String, val args: Map<String, Any>) : WorkflowStep
}