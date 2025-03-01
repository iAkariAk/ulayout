package com.akari.ulayout.util

import okio.Path.Companion.toPath

fun String.pathNormalize(): String = toPath(true).toString()