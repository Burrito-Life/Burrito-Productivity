package com.burrito.productivity.ai.orchestration

/**
 * Represents the state of the interaction between the User and the Agent.
 */
sealed class InteractionState {
    data object Idle : InteractionState()
    data object Thinking : InteractionState()
    data object AwaitingUserApproval : InteractionState()
    data object ExecutingTask : InteractionState()
}
