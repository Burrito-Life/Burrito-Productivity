package com.burrito.productivity.data.local

/**
 * Utility to pack/unpack Kotlin objects into the JSONB binary format for SQLite.
 */
class JsonbConverter {
    // TODO: use kotlinx.serialization instead of what's here
    
    fun <T> toJson(data: T): ByteArray {
        // Placeholder: return empty byte array
        return ByteArray(0)
    }

    fun <T> fromJson(bytes: ByteArray, clazz: Any): T? {
        // Placeholder: return null
        return null
    }
}
