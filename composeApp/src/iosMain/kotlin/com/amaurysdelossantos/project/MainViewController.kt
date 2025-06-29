package com.amaurysdelossantos.project

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.amaurysdelossantos.project.di.targetModule
import com.amaurysdelossantos.project.navigation.RootComponent
import com.amaurysdelossantos.project.navigation.RootComponent.Configuration
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {

    startKoin {
        modules(targetModule) // or listOf(...) if needed
    }
    val navigation = StackNavigation<Configuration>()

    val root = remember {
        RootComponent(DefaultComponentContext(LifecycleRegistry()), navigation)
    }
    App(root)
}