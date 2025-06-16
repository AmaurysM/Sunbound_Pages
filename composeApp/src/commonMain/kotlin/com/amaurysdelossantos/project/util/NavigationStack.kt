package com.amaurysdelossantos.project.util

import com.amaurysdelossantos.project.navigation.RootComponent.Configuration
import com.arkivanov.decompose.router.stack.StackNavigation

object NavigationHolder {
    val navigation = StackNavigation<Configuration>()
}