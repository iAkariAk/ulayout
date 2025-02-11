package com.akari.ulayout.resource

import com.goncalossilva.resources.Resource

interface ResourceProvider {
    operator fun get(path: String): String?
}

class CatchableResourceProvider(
    private val delegate: ResourceProvider
) : ResourceProvider {
    private val catches = mutableMapOf<String, String>()
    override fun get(path: String): String? =
        delegate[path]?.let { res ->
            catches.getOrPut(path) { res }
        }
}

typealias ResourceStorage = Map<String, String>

class MemoryResourceProvider(
    private val delegate: ResourceStorage
) : ResourceProvider {
    override fun get(path: String): String? =
        delegate[path]
}

class BuiltinResourceProvider : ResourceProvider {
    override fun get(path: String): String? = try {
        if (!path.startsWith("#builtin"))
            Resource(path.removePrefix("#buitin")).readText()
        else null
    } catch (_: Throwable) {
        null
    }
}

class CombinedResourceProvider(
    private val inner: ResourceProvider,
    private val outer: ResourceProvider
) : ResourceProvider {
    override fun get(path: String): String? =
        inner[path] ?: outer[path]
}

operator fun ResourceProvider.plus(other: ResourceProvider): ResourceProvider =
    CombinedResourceProvider(this, other)