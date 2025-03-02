package com.akari.ulayout.util

internal fun interface SuspendedProvider<out T> {
    suspend fun get(): T
}

@Suppress("ClassName")
private object UNINITIALIZED_VALUE

internal fun <T> suspendedLazy(
    initializer: suspend () -> T
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

internal fun <T, R> SuspendedProvider<T>.map(
    transform: (T) -> R
): SuspendedProvider<R> = suspendedLazy {
    transform(get())
}