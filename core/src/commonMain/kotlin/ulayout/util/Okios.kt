package ulayout.util

import okio.Path
import okio.Path.Companion.toPath

internal fun Path.normalizeToString(): String = normalized().toString()
internal fun Path.appendSuffix(suffix: String): Path =
    (toString() + suffix).toPath()
