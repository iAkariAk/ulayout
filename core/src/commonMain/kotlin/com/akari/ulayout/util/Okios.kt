package com.akari.ulayout.util

import okio.Path
import okio.Path.Companion.toPath

internal fun Path.normalizeToString(): String = normalized().toString()

internal fun Path.appendSuffix(suffix: String): Path =
    (toString() + suffix).toPath()

internal operator fun Path.contains(other: Path): Boolean =
    normalizeToString().endsWith(other.normalizeToString())