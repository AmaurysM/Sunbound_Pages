package com.amaurysdelossantos.project.util

// androidMain/kotlin/com/amaurysdelossantos/project/util/AppDirectories.kt
// androidMain/kotlin/com/amaurysdelossantos/project/util/AppDirectories.kt
import android.content.Context

lateinit var androidAppContext: Context
    private set

fun initAppContext(context: Context) {
    androidAppContext = context.applicationContext
}

actual object AppDirectories {
    actual fun getAppStoragePath(): String {
        val context = androidAppContext
        return context.filesDir.absolutePath
    }
}



