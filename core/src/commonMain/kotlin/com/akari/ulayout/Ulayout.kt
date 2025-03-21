@file:OptIn(DelicateCoroutinesApi::class)

package com.akari.ulayout

import com.akari.ulayout.component.VisualComponent
import com.akari.ulayout.component.findContained
import com.akari.ulayout.graphics.Font
import com.akari.ulayout.graphics.bounds
import com.akari.ulayout.graphics.drawImageWithScale
import com.akari.ulayout.intent.Intent
import com.akari.ulayout.intent.ScreenIntents
import com.akari.ulayout.resource.getImage
import com.akari.ulayout.template.TemplateProvider
import com.akari.ulayout.template.expandAll
import com.akari.ulayout.ulpack.Route
import com.akari.ulayout.ulpack.Style
import com.akari.ulayout.ulpack.UlayoutConfigure
import com.akari.ulayout.ulpack.Ulpack
import kotlinx.browser.window
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.Path.Companion.toPath
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.pointerevents.PointerEvent
import kotlin.properties.Delegates

fun interface AppCallbacks {
    suspend fun onIntent(intent: Intent): Boolean
}

infix fun AppCallbacks.then(other: AppCallbacks) = AppCallbacks { intent ->
    this.onIntent(intent) || other.onIntent(intent)
}

suspend fun Ulayout(
    canvas: HTMLCanvasElement,
    configure: UlayoutConfigure,
    start: Boolean = true,
    fonts: List<Font> = UlayoutDefaults.fonts,
    callbacksBuilder: ((default: AppCallbacks) -> AppCallbacks)? = null
): Ulayout {
    var ulayout: Ulayout by Delegates.notNull()
    val defaultCallbacks = UlayoutDefaults.callbacks { ulayout }
    ulayout = Ulayout(
        canvas = canvas,
        ulpack = Ulpack.wrap(configure),
        fonts = fonts,
        callbacks = callbacksBuilder?.invoke(defaultCallbacks) ?: defaultCallbacks
    )
    if (start) ulayout.start()
    return ulayout
}


suspend fun Ulayout(
    canvas: HTMLCanvasElement,
    ulpack: Ulpack,
    start: Boolean = true,
    fonts: List<Font> = UlayoutDefaults.fonts,
    callbacksBuilder: ((default: AppCallbacks) -> AppCallbacks)? = null
): Ulayout {
    var ulayout: Ulayout by Delegates.notNull()
    val defaultCallbacks = UlayoutDefaults.callbacks { ulayout }
    ulayout = Ulayout(
        canvas = canvas,
        ulpack = ulpack,
        fonts = fonts,
        callbacks = callbacksBuilder?.invoke(defaultCallbacks) ?: defaultCallbacks
    )
    if (start) ulayout.start()
    return ulayout
}

class Ulayout(
    private val canvas: HTMLCanvasElement,
    private val ulpack: Ulpack,
    private val callbacks: AppCallbacks,
    private val fonts: List<Font> = UlayoutDefaults.fonts
) {
    companion object {}

    private val configure = ulpack.configure
    private val context = canvas.getContext("2d") as CanvasRenderingContext2D
    private val env = Environment(
        templates = TemplateProvider(),
        resources = ulpack
    )
    private var currentRoute: Route? = configure.routes[configure.initRoute]
    private var recomposeCount = 0
    private lateinit var availableComponents: List<VisualComponent>

    init {
        GlobalScope.launch {
            canvas.addEventListener(
                "click",
                { event ->
                    GlobalScope.launch {
                        event as PointerEvent
                        availableComponents.findContained(
                            event.offsetX,
                            event.offsetY
                        )?.onClick(callbacks)
                    }
                })
            env.templates += Res.templates.get()
            env.templates += configure.templates
        }
    }

    suspend fun start() {
        val document = checkNotNull(canvas.ownerDocument)
        fonts.forEach { it.attachTo(document) }
        recompose()
    }

    private fun getAvailableComponents(): List<VisualComponent> {
        val components = configure.common + (currentRoute?.components ?: emptyList())
        return components.expandAll(env.templates)
    }

    suspend fun recompose(newRoute: String? = null) {
        if (recomposeCount > 0 && newRoute == currentRoute?.name) return // skip
        currentRoute = configure.routes[newRoute]
        availableComponents = getAvailableComponents()
        context.fillBackground()
        availableComponents.forEach { drawComponent(it) }
        recomposeCount++
    }

    private suspend fun CanvasRenderingContext2D.fillBackground() {
        val path = currentRoute?.background ?: configure.style.background
        val background = ulpack.getImage(path.toPath()).get()
        drawImageWithScale(
            background.image,
            canvas.bounds,
            background.scaleDescription
        )
    }

    private suspend fun drawComponent(component: VisualComponent) {
        component.init(env)
        component.paint(context)
    }
}

object UlayoutDefaults {
    val style = Style.Default
    val fonts = listOf(
        Font(
            family = "ZhanKuKuaiLeTi2016",
            paths = listOf(
                "./assets/ulayout/font/ZhanKuKuaiLeTi2016.woff2",
                "./assets/ulayout/font/ZhanKuKuaiLeTi2016.ttf",
            )
        )
    )

    fun callbacks(ulayoutProvider: () -> Ulayout) = object : AppCallbacks {
        val ulayout get() = ulayoutProvider()
        override suspend fun onIntent(intent: Intent): Boolean = when (intent) {
            is ScreenIntents.Alert -> {
                if (intent.onConfirm != null) {
                    if (window.confirm(intent.message)) onIntent(intent.onConfirm)
                } else {
                    window.alert(intent.message)
                }
                true
            }

            is ScreenIntents.Navigate -> {
                ulayout.recompose(intent.destination)
                true
            }

            else -> false
        }
    }
}
