package com.burrito.productivity.ai.context

/**
 * Shared Semantic Environment (SSE).
 * Holds the current context of the user interaction, including recent history and active tasks.
 * TODO: Define the structure of RAG context, such as relevant data blocks, documents, etc.
 * TODO: Flesh out the implementation of SSE throughout the app, for maximal intelligence
 */
data class RAGContext(
    val data: Map<String, Any> = emptyMap()
)
