package com.akari.ulayout.resource

import com.goncalossilva.resources.Resource
import okio.Path.Companion.toPath

internal abstract class Resources(
    protected val rootPath: String = ""
) {
    constructor(parent: String, path: String) : this((parent.toPath() / path.toPath()).normalized().toString())
    constructor(parent: Resources, path: String) : this(parent.rootPath, path)

    private fun normalizePath(path: String) = (rootPath.toPath() / path.toPath()).normalized().toString()
    protected fun text(path: String) = Resource(normalizePath(path)).readText()
    protected fun bytes(path: String) = Resource(normalizePath(path)).readBytes()
    protected fun image(path: String) = ImageResource.fromAssets(normalizePath(path))
}