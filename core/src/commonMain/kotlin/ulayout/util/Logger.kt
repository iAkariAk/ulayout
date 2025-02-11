package com.akari.ulayout.util

internal fun logInvoke(funName:String, vararg args: dynamic) {
    println("[FUN_INVOKE] $funName(${args.joinToString(",")})")
}