// Rich data objects for the workflow
package com.burrito.productivity.ai.agents

data class ReasoningResponse(
    val text: String,
    val a2ui: Map<String, Any>? = null, // TODO: Actual A2UI Implementation
    val metadata: Map<String, Any> = emptyMap() // TODO: Determine what kinds of metadata will be used & how - maybe it shouldn't be lumped into a Map here, if there is anything we'll use regularly
)

data class CritiqueResponse(
    val passed: Boolean,
    val feedback: String,
    val score: Int
)

// TODO: Implement this with properties that make sense - returned within the response from TriageAgent.triage() and passed to subsequent Agent calls - may need to be defined in its own file to avoid dependency issues
data class RAGContext()