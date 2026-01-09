package com.burrito.productivity.data.mcp

/**
 * Interface representing a Model Context Protocol (MCP) Tool.
 * These are the "Hands" of the agent.
 */
interface MCPTool {
    val name: String
    val description: String
    fun execute(args: Map<String, Any>): Any
}

/**
 * Central registry for MCP tools.
 * Allows for dynamic dispatch and plugin-like functionality.
 */
class MCPToolRegistry {
    private val tools = mutableMapOf<String, MCPTool>()

    fun register(tool: MCPTool) {
        tools[tool.name] = tool
    }

    fun getTool(name: String): MCPTool? = tools[name]
    
    fun getAllTools(): List<MCPTool> = tools.values.toList()
}
