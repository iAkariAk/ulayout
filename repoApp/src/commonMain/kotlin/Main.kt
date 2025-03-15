@file:OptIn(ExperimentalComposeUiApi::class)


import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.akari.ulpack.repoapp.App

fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") { App() }
}