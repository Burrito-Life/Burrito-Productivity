package com.burrito.productivity.ai.orchestration

/**
 * Registry that maps custom JSON tags from the AI response to specific UI layouts/components.
 * Enables "Advanced Customization Support" by allowing users to "install" new UI capabilities.
 */
object A2UIComponentRegistry {
    private val componentMap = mutableMapOf<String, Any>() // Any is placeholder for Composable wrapper

    fun register(tag: String, component: Any) {
        componentMap[tag] = component
    }

    fun getComponent(tag: String): Any? = componentMap[tag]
}
