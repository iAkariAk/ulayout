package com.akari.ulayout.resource

import com.akari.ulayout.util.pathNormalize
import com.goncalossilva.resources.Resource

interface ResourceProvider {
    operator fun get(path: String): String?
    suspend fun getImage(path: String): ImageResource = ImageResource.fromData(
        imageSrcData = checkNotNull(get(path)) { "Resource $path not found" },
        descriptionJson = try {
            checkNotNull(get("$path.sd.json")) { "Resource $path.sd.json not found" }
        } catch (_: Throwable) {
            null
        }
    ).get()
}

class CatchableResourceProvider(
    private val delegate: ResourceProvider
) : ResourceProvider {
    private val catches = mutableMapOf<String, String>()
    override fun get(path: String): String? =
        delegate[path]?.let { res ->
            catches.getOrPut(path) { res }
        }.also {
            println("catch: $path")
        }
}

typealias ResourceStorage = Map<String, String>

class MemoryResourceProvider(
    private val delegate: ResourceStorage
) : ResourceProvider {
    override fun get(path: String): String? =
        delegate[path.pathNormalize()]
}

class BuiltinResourceProvider : ResourceProvider {
    override fun get(path: String): String? = try {
        if (path.startsWith("#builtin:"))
            Resource(path.removePrefix("#buitin:").pathNormalize()).readText()
        else null
    } catch (_: Throwable) {
        null
    }
}

class CombinedResourceProvider(
    private val inner: ResourceProvider,
    private val outer: ResourceProvider
) : ResourceProvider {
    override fun get(path: String): String? {
        val p = path.pathNormalize()
        return inner[p] ?: outer[p]
    }
}

operator fun ResourceProvider.plus(other: ResourceProvider): ResourceProvider =
    CombinedResourceProvider(this, other)

internal abstract class Resources(
    protected val pathPrefix: String = ""
) {
    protected fun image(path: String) = ImageResource.fromAssets("$pathPrefix$path".pathNormalize())
}