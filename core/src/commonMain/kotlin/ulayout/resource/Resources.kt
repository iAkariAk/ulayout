package com.akari.ulayout.resource

import okio.Path.Companion.toPath
import ulayout.resource.ResourceAccessor

internal abstract class Resources(
    protected val rootPath: String = ""
) {
    constructor(parent: String, path: String) : this((parent.toPath() / path.toPath()).normalized().toString())
    constructor(parent: Resources, path: String) : this(parent.rootPath, path)

    private fun normalizePath(path: String) = (rootPath.toPath() / path.toPath()).normalized().toString()
    protected fun text(path: String) = ResourceAccessor.readText(normalizePath(path).toPath())
    protected fun bytes(path: String) = ResourceAccessor.readBytes(normalizePath(path).toPath())
    protected fun image(path: String) = ImageResource.fromAssets(normalizePath(path))
}