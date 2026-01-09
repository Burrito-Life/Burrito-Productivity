package com.burrito.productivity.ui

import com.burrito.productivity.ai.agents.Types.RevisedResponse
import com.burrito.productivity.ai.ui.A2UIComponentRegistry

/**
 * Standardizes the "look and feel" of the Burrito brand.
 * Separates UI/UX decisions from AI model calls.
 */
object BurritoUI {

    sealed class UIRepresentation {
        data class Text(val content: String) : UIRepresentation()
        data class Component(val component: Any, val data: Map<String, Any>) : UIRepresentation()
        data class Error(val message: String) : UIRepresentation()
    }

    fun buildUIRepresentationForAIResponse(response: RevisedResponse): UIRepresentation {
        val a2uiData = response.a2ui
        
        return if (a2uiData != null && a2uiData.containsKey("type")) {
            val type = a2uiData["type"] as String
            hydrateA2UI(type, a2uiData)
        } else {
            // Default to simple text representation if no A2UI data is present
            UIRepresentation.Text(response.finalContent)
        }
    }

    private fun hydrateA2UI(type: String, data: Map<String, Any>): UIRepresentation {
        val component = A2UIComponentRegistry.getComponent(type)
        return if (component != null) {
            UIRepresentation.Component(component, data)
        } else {
            // Fallback if component not found, or maybe log a warning
            UIRepresentation.Text("Display Error: Unknown component type '$type'")
        }
    }
}
