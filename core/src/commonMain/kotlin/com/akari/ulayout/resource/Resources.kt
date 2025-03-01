package com.akari.ulayout.resource

import com.akari.ulayout.resource.accessor.ResourceAccessor
import okio.Path
import okio.Path.Companion.toPath

internal abstract class Resources(
    protected val rootPath: Path = ".".toPath()
) {
    constructor(parent: Path, path: Path) : this(parent / path)
    constructor(parent: Resources, path: Path) : this(parent.rootPath, path)

    constructor(rootPath: String) : this(rootPath.toPath())
    constructor(parent: String, path: String) : this(parent.toPath(), path.toPath())
    constructor(parent: Resources, path: String) : this(parent.rootPath, path.toPath())

    private fun absPath(path: String) = rootPath / path.toPath()
    protected fun text(path: String) = ResourceAccessor.readText(absPath(path))
    protected fun bytes(path: String) = ResourceAccessor.readBytes(absPath(path))
    protected fun image(path: String) = ResourceAccessor.getImage(absPath(path))
}