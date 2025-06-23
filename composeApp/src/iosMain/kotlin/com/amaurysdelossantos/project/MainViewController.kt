package com.amaurysdelossantos.project

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.amaurysdelossantos.project.di.targetModule
import com.amaurysdelossantos.project.navigation.RootComponent
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {

    startKoin {
        modules(targetModule) // or listOf(...) if needed
    }

    val root = remember {
        RootComponent(DefaultComponentContext(LifecycleRegistry()))
    }
    App(root)
}