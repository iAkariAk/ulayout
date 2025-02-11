package com.akari.ulayout.util

fun interface SuspendedProvider<out T> {
    suspend fun get(): T
}

private object UNINITIALIZED_VALUE

inline fun <T> suspendedLazy(
    crossinline initializer: suspend () -> T
) = object : SuspendedProvider<T> {
    var value: Any? = UNINITIALIZED_VALUE
    override suspend fun get(): T {
        println("TRY GET")
        if (value == UNINITIALIZED_VALUE) {
            println("suspendedLazy")
            value = initializer()
        }
        @Suppress("UNCHECKED_CAST")
        return value as T
    }
}