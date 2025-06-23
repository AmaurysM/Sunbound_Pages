package com.amaurysdelossantos.project

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.amaurysdelossantos.project.database.getDatabaseBuilder
import com.amaurysdelossantos.project.di.initializeKoin
import com.amaurysdelossantos.project.di.targetModule
import com.amaurysdelossantos.project.navigation.RootComponent
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.koin.core.context.GlobalContext.startKoin

fun main() = application {

    val lifecycle = LifecycleRegistry()
    //val database = getDatabaseBuilder()

    initializeKoin()


    val root = remember {
        RootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle)
        )
    }

    val windowState = rememberWindowState()

    LifecycleController(lifecycle, windowState)

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Sunbound Pages",
    ) {
        App(root)
    }
}