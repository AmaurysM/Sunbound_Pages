package com.amaurysdelossantos.project

import androidx.compose.runtime.Composable


expect val world: String

@Composable
expect fun PlatformFunction()

//class Greeting {
//    private val platform = getPlatform()
//
//    fun greet(): String {
//        return "Hello, ${platform.name}!"
//    }
//}