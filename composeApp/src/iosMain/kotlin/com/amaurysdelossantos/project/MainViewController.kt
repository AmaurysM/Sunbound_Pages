package com.amaurysdelossantos.project

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.amaurysdelossantos.project.navigation.RootComponent
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

fun MainViewController() = ComposeUIViewController {
    val root = remember {
        RootComponent(DefaultComponentContext(LifecycleRegistry()))
    }
    App(root)
}