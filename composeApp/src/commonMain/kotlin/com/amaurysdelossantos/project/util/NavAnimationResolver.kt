package com.amaurysdelossantos.project.util

import com.amaurysdelossantos.project.navigation.RootComponent.Configuration
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.slide

private val animationMap =
    mapOf(
        (Configuration.Library to Configuration.OnMyDevice) to slide(),
        (Configuration.OnMyDevice to Configuration.Library) to slide(),

        (Configuration.OnMyDevice to Configuration.BookView) to slide(),
        (Configuration.BookView to Configuration.OnMyDevice) to slide(),


        (Configuration.Reading to Configuration.ReadingEBookView("dummy")) to slide(),
        (Configuration.ReadingEBookView("dummy") to Configuration.Reading) to slide(),

        )

fun resolveAnimationMapBased(
    from: Configuration?,
    to: Configuration
): StackAnimator {
    val key = animationMap.keys.find {
        it.first == from && it.second::class == to::class
    }
    return (animationMap[key] ?: fade())
}