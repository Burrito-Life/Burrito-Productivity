package com.burrito.productivity.ai.agents

/**
 * Defines the contract for an AI Agent's capability.
 * This interface represents a "Signature" in the DSPy sense -
 * defining the input and output structure for a specific reasoning task.
 * TODO: Shouldn't this be more robust? I assumed DSPy has something more robust than two strings...
 */
interface AgentSignature {
    val name: String
    val description: String
}
