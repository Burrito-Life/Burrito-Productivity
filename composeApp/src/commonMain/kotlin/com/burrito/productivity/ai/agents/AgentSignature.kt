package com.burrito.productivity.ai.agents

/**
 * Defines the contract for an AI Agent's capability.
 * This interface represents a "Signature" in the DSPy sense -
 * defining the input and output structure for a specific reasoning task.
 */
interface AgentSignature {
    val name: String
    val description: String
}
