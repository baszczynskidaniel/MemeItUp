package org.example.project

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.example.project.app.App
import org.example.project.di.initKoin


fun main() = application {
    initKoin()
    val state = rememberWindowState(
        width = 2000.dp,
        height = 1000.dp,
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = "MemeItUp",
        state = state
    ) {
        App()
    }
}