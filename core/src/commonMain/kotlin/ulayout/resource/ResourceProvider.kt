package com.akari.ulayout.resource

import com.akari.ulayout.util.pathNormalize
import com.akari.ulayout.util.readTextOrNull
import com.goncalossilva.resources.Resource
import okio.Path.Companion.toPath

interface ResourceProvider {
    operator fun get(path: String): String?
    suspend fun getImage(path: String): ImageResource?
}

class CatchableResourceProvider(
    private val delegate: ResourceProvider
) : ResourceProvider {
    private val catchesOfGet = mutableMapOf<String, String>()
    private val catchesOfGetImage = mutableMapOf<String, ImageResource>()
    override fun get(path: String): String? {
        return catchesOfGet.getOrPut(path) {
            delegate[path] ?: return null
        }
    }

    override suspend fun getImage(path: String): ImageResource? {
        return catchesOfGetImage.getOrPut(path) {
            delegate.getImage(path) ?: return null
        }
    }

    override fun toString() = "CatchableResourceProvider($delegate)"
}

typealias ResourceStorage = Map<String, String>

class MemoryResourceProvider(
    private val delegate: ResourceStorage
) : ResourceProvider {
    override fun get(path: String): String? =
        delegate[path.pathNormalize()]

    override suspend fun getImage(path: String): ImageResource? {
        return ImageResource.fromData(
            imageSrcData = get(path) ?: return null,
            descriptionJson = get("$path.sd.json")
        ).get()
    }

    override fun toString() = "MemoryResourceProvider"
}

class BuiltinResourceProvider : ResourceProvider {
    private fun getActualPathOrNull(path: String): String? =
        path.takeIf { path.startsWith("builtin@") }
            ?.removePrefix("builtin@")
            ?.let { ("assets/ulayout/".toPath() / it.toPath()).normalized().toString() }

    override fun get(path: String): String? =
        getActualPathOrNull(path)
            ?.let(::Resource)
            ?.readTextOrNull()

    override suspend fun getImage(path: String): ImageResource? {
        return ImageResource.fromData(
            imageSrcData = getActualPathOrNull(path) ?: return null,
            descriptionJson = get("$path.sd.json")
        ).get()
    }

    override fun toString() = "BuiltinResourceProvider"
}

class CombinedResourceProvider(
    private val inner: ResourceProvider,
    private val outer: ResourceProvider
) : ResourceProvider {
    override fun get(path: String): String? {
        val p = path.pathNormalize()
        return (inner[p] ?: outer[p])
    }

    override suspend fun getImage(path: String): ImageResource? =
        inner.getImage(path) ?: outer.getImage(path)

    override fun toString() = "CombinedResourceProvider($inner, $outer)"
}

operator fun ResourceProvider.plus(other: ResourceProvider): ResourceProvider =
    CombinedResourceProvider(this, other)

