package com.burrito.productivity.data.local

/**
 * Abstraction for the raw I/O to the local database.
 * This effectively acts as a lower-level facade (than the AppDatabase) over SQLDelight, sqlite-vec, and sqlite core (and any other storage tools we use in the future).
 * */
interface DbService {
    suspend fun executeQuery(query: String): List<String>,
    suspend fun executeUpdate(query: String): Int,
    suspend fun executeTransaction(block: () -> Unit)
}

class DbServiceImpl(
    private val sqliteKey: String
) : DbService {
    override suspend fun executeQuery(query: String): List<String> {
        // TODO: Implement real query execution
        return emptyList()
    }
    override suspend fun executeUpdate(query: String): Int {
        // TODO: Implement real update execution
        return 0
    }
    override suspend fun executeTransaction(block: () -> Unit) {
        // TODO: Implement real transaction execution
    }
}