package com.akari.ulayout.component

import com.akari.ulayout.AppCallbacks
import com.akari.ulayout.Environment
import com.akari.ulayout.graphics.Rect
import com.akari.ulayout.graphics.rect
import com.akari.ulayout.intent.Events
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.w3c.dom.CanvasRenderingContext2D

@Serializable
sealed class Component {
    abstract val x: Int
    abstract val y: Int

    @Transient
    private var _env: Environment? = null
    internal val env
        get() = checkNotNull(_env) {
            "Component is not initialized"
        }

    internal fun init(env: Environment) {
        _env = env
    }
}


@Serializable
sealed class VisualComponent : Component() {
    abstract val width: Int
    abstract val height: Int
    abstract val events: Map<String, String>

    suspend fun onClick(callbacks: AppCallbacks) {
        val events = Events.parse(events)
        events.onClick?.let { callbacks.onIntent(it) }
    }

    open suspend fun paint(context: CanvasRenderingContext2D) {
//        Debug
//        context.fillStyle = "#403330"
//        context.fillRect(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble())
    }

    protected inline fun CanvasRenderingContext2D.paintToDst(action: CanvasRenderingContext2D.(dst: Rect) -> Unit) {
        val dst = bounds
        save()
        rect(dst)
        clip()
        action(dst)
        restore()
    }

    fun getIfContained(x: Double, y: Double) = takeIf {
        bounds.contains(x, y)
    }

    fun getIfContained(rect: Rect): VisualComponent? =
        takeIf {
            bounds.contains(rect)
        }
}

val VisualComponent.bounds
    get() =
        Rect.makeXYWH(this.x.toDouble(), this.y.toDouble(), width.toDouble(), height.toDouble())

fun List<VisualComponent>.findContained(x: Double, y: Double): VisualComponent? =
    mapNotNull { it.getIfContained(x, y) }.lastOrNull()
