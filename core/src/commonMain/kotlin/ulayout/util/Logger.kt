package com.akari.ulayout.util

fun logInvoke(funName:String, vararg args: dynamic) {
    println("[FUN_INVOKE] $funName(${args.joinToString(",")})")
}