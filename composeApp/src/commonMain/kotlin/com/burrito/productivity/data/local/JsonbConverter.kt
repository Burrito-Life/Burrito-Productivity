package com.burrito.productivity.data.local

/**
 * Utility to pack/unpack Kotlin objects into the JSONB binary format for SQLite.
 */
class JsonbConverter {
    // In a real app, this would use kotlinx.serialization
    
    fun <T> toJson(data: T): ByteArray {
        // Placeholder: return empty byte array
        return ByteArray(0)
    }

    fun <T> fromJson(bytes: ByteArray, clazz: Any): T? {
        // Placeholder: return null
        return null
    }
}
