package com.amaurysdelossantos.project.util

import java.nio.file.Paths

actual object AppDirectories {
    actual fun getAppStoragePath(): String {
        val userHome = System.getProperty("user.home")
        val appDir = Paths.get(userHome, ".myBookApp").toFile()
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        return appDir.absolutePath // Example: /Users/you/.myBookApp
    }
}