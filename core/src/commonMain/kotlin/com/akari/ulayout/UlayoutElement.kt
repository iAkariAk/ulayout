@file:OptIn(DelicateCoroutinesApi::class)

package com.akari.ulayout

import com.akari.ulayout.dom.UrlSource
import com.akari.ulayout.ulpack.Ulpack
import com.akari.ulayout.util.jsFunction
import kotlinx.browser.document
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLCanvasElement

fun Ulayout.Companion.define(name: String = "ul-layout") {
    val canvas = document.createElement("canvas")
        .unsafeCast<HTMLCanvasElement>()
        .apply {
            width = 800
            height = 480
        }
    val wrap = jsFunction<(HTMLCanvasElement, (String, dynamic, dynamic) -> Unit) -> HTMLCanvasElement>(
        "_canvas", "_attributeChangedCallback",
        block = """
            let element = class extends HTMLElement {
            static observedAttributes = ["ulpack"];
               constructor() {
                   super();
                   this.replaceWith(_canvas);
             }
             
              attributeChangedCallback(name, oldValue, newValue) {
                  _attributeChangedCallback(name, oldValue, newValue);
              }
 
            };
        customElements.define('$name', element);
    """.trimIndent()
    ) // Oh shit
    var job: Job? = null
    fun attachToCanvas(ulpack: Ulpack) {
        job?.cancel()
        job = GlobalScope.launch {
            Ulayout(canvas, ulpack)
        }
    }
    wrap(canvas) { name, oldValue, newValue ->
        GlobalScope.launch {
            if (name != "ulpack") return@launch
            val src = UrlSource(newValue.unsafeCast<String>())
            val ulpack = Ulpack.fromZip(src)
            attachToCanvas(ulpack)
        }
    }
//    attachToCanvas()
}