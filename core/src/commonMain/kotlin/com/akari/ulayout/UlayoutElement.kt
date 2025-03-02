@file:OptIn(DelicateCoroutinesApi::class)

package com.akari.ulayout

import com.akari.ulayout.dom.UrlSource
import com.akari.ulayout.intent.LevelIntents
import com.akari.ulayout.intent.ScreenIntents
import com.akari.ulayout.ulpack.Ulpack
import com.akari.ulayout.util.jsFunction
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import org.w3c.dom.HTMLCanvasElement

fun Ulayout.Companion.define(name: String = "ul-layout") {
    val canvas = document.createElement("canvas")
        .unsafeCast<HTMLCanvasElement>().apply {
            width = 800
            height = 480
        }

    val wrap = jsFunction<(HTMLCanvasElement, (String, dynamic, dynamic) -> Unit) -> Unit>(
        "_canvas", "_attributeChangedCallback",
        block = """
            let element = class extends HTMLElement {
                static observedAttributes = ["ulpack"];
                constructor() {
                    super();
                    this.canvas = _canvas;
                }
                connectedCallback() {
                    if (!this.contains(this.canvas)) {
                        this.appendChild(this.canvas);
                    }
                }            
                attributeChangedCallback(name, oldValue, newValue) {
                    _attributeChangedCallback(name, oldValue, newValue);
                }
            };
            customElements.define('$name', element);
        """.trimIndent()
    )

    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    var job: Job? = null

    fun attachToCanvas(ulpack: Ulpack) {
        job?.cancel()
        job = scope.launch {
            Ulayout(canvas, ulpack) { callback ->
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
    }

    wrap(canvas) { name, _, newValue ->
        if (name != "ulpack") return@wrap
        val srcValue = newValue?.toString() ?: return@wrap

        scope.launch {
            val ulpack = Ulpack.fromZip(UrlSource(srcValue))
            attachToCanvas(ulpack)
        }
    }

    // Don't give it a initial value
//    scope.launch {
//        attachToCanvas(Ulpack.fromZip(UrlSource("./test.ulpack")))
//    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun setup() {
    Ulayout.define()
}
