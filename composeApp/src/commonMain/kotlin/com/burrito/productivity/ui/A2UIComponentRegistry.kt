package com.burrito.productivity.ai.ui

/**
 * Registry that maps custom JSON tags from the AI response to specific UI layouts/components.
 * Enables "Advanced Customization Support" by allowing users to "install" new UI capabilities.
 * TODO: Flesh out a complete implementation of A2UI - & Consider how this fits with the BurritoUI class for standardizing "look and feel" across Burrito brand apps
 */
object A2UIComponentRegistry {
    private val componentMap = mutableMapOf<String, Any>() // Any is placeholder for Composable wrapper

    fun register(tag: String, component: Any) {
        componentMap[tag] = component
    }

    fun getComponent(tag: String): Any? = componentMap[tag]
}
