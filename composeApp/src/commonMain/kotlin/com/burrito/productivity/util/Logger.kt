package com.burrito.productivity.util

/**
 * Common logging utility.
 */
object Logger {
    fun log(tag: String, message: String) {
        println("[$tag] $message")
    }
    
    fun error(tag: String, message: String, throwable: Throwable? = null) {
        println("ERROR: [$tag] $message")
        throwable?.printStackTrace()
    }
}
