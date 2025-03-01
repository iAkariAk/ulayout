@file:OptIn(ExperimentalJsExport::class)

package com.akari.ulayout.util

@JsName("Function")
internal external fun <T> jsFunction(vararg params: String, block: String): T
