package com.akari

import com.akari.ulayout.AppCallbacks
import com.akari.ulayout.Ulayout
import com.akari.ulayout.intent.LevelIntents
import com.akari.ulayout.intent.ScreenIntents
import com.akari.ulayout.then
import com.akari.ulayout.ulpack.UlayoutConfigure
import com.akari.ulayout.ulpack.Ulpack
import com.goncalossilva.resources.Resource
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.dom.append
import kotlinx.html.h1
import kotlinx.html.h3
import kotlinx.html.id
import kotlinx.html.js.canvas
import kotlinx.html.js.div
import kotlinx.html.style
import org.w3c.dom.HTMLCanvasElement

suspend fun main() {
    runCatching {
        runApp()
    }.onFailure { e ->
        document.body!!.append {
            console.error(e.message)
            h1 {
                style = "color: red;"
                +(e.message ?: "<Null>")
            }
            h3 {
                +e.stackTraceToString()
            }
        }
    }
}

private suspend fun runApp() {
    document.body!!.append {
        div {
            style = """
                 width: 100%;
                 height: 100%;
                 display: flex;
                 justify-content: center;
                 align-items: center;
            """.trimIndent()
            canvas {
                style = """
                border: 1px solid black;
            """.trimIndent()

                id = "content"
                width = "800px"
                height = "480px"
            }
        }
    }

    val canvas = document.getElementById("content") as HTMLCanvasElement
    val configureJson = Resource("configure.json").readText()
    val configure = UlayoutConfigure.parse(configureJson)
    Ulayout(
        canvas = canvas,
        ulpack = Ulpack.wrap(configure) {
            putTestRes()
        }
    ) { callback ->
        AppCallbacks { intent ->
            when (intent) {
                is LevelIntents.Goto -> {
                    window.alert("Want to go to ${intent.destination}.")
                    true
                }

                is ScreenIntents.Navigate -> {
                    window.alert("Want to navigate to ${intent.destination}.")
                    callback.onIntent(intent)
                }

                ScreenIntents.Exit -> {
                    window.alert("Want to exit.")
                    true
                }

                else -> false
            }
        } then callback
    }
}
