package com.burrito.productivity.ai.context

/**
 * Shared Semantic Environment (SSE).
 * Holds the current context of the user interaction, including recent history and active tasks.
 */
data class SemanticContext(
    val recentHistory: List<String> = emptyList(),
    val activeTask: String? = null
)
