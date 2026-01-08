package com.burrito.productivity.data.mcp

/**
 * Interface representing a Model Context Protocol (MCP) Tool.
 * These are the "Hands" of the agent.
 */
interface McpTool {
    val name: String
    val description: String
    fun execute(args: Map<String, Any>): Any
}

/**
 * Central registry for MCP tools.
 * Allows for dynamic dispatch and plugin-like functionality.
 */
class McpToolRegistry {
    private val tools = mutableMapOf<String, McpTool>()

    fun register(tool: McpTool) {
        tools[tool.name] = tool
    }

    fun getTool(name: String): McpTool? = tools[name]
    
    fun getAllTools(): List<McpTool> = tools.values.toList()
}
