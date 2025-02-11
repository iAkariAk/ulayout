package com.akari.ulayout.util

fun interface SuspendedProvider<out T> {
    suspend fun get(): T
}

@Suppress("ClassName")
private object UNINITIALIZED_VALUE

inline fun <T> suspendedLazy(
    crossinline initializer: suspend () -> T
) = object : SuspendedProvider<T> {
    private var value: Any? = UNINITIALIZED_VALUE
    override suspend fun get(): T {
        if (value == UNINITIALIZED_VALUE) {
            value = initializer()
        }
        @Suppress("UNCHECKED_CAST")
        return value as T
    }

    override fun toString() = "SuspendedLazyProvider"
}