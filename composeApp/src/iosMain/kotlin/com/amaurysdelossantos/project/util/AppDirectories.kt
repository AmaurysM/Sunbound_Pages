package com.amaurysdelossantos.project.util

// commonMain
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

actual object AppDirectories {
    actual fun getAppStoragePath(): String {
        val paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)
        return paths.firstOrNull() as? String ?: error("Failed to find documents directory")
    }
}
