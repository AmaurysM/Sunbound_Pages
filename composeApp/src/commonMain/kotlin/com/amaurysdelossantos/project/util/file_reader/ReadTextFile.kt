package com.amaurysdelossantos.project.util.file_reader

import co.touchlab.kermit.Logger
import okio.FileSystem
import okio.Path.Companion.toPath

expect val systemFileSystem: FileSystem

fun readTextFile(filePath: String): String? {
    return try {
        val path = filePath.toPath()

        // Check if file exists first
        if (!systemFileSystem.exists(path)) {
            Logger.e { "File does not exist at path: $filePath" }
            return null
        }

        // Check if it's actually a file (not directory)
        val metadata = systemFileSystem.metadata(path)
        if (!metadata.isRegularFile) {
            Logger.e { "Path is not a regular file: $filePath" }
            return null
        }

        // Read the file
        val content = systemFileSystem.read(path) {
            readUtf8()
        }

        Logger.d { "Successfully read file: $filePath (${content.length} characters)" }
        return content

    } catch (e: Exception) {
        Logger.e(e) { "Failed to read file at path '$filePath': ${e.message}" }
        return null
    }
}

// Helper function to check if file exists
fun fileExists(filePath: String): Boolean {
    return try {
        systemFileSystem.exists(filePath.toPath())
    } catch (e: Exception) {
        Logger.e(e) { "Error checking if file exists: $filePath" }
        false
    }
}

// Helper function to get file size
fun getFileSize(filePath: String): Long? {
    return try {
        val path = filePath.toPath()
        if (systemFileSystem.exists(path)) {
            systemFileSystem.metadata(path).size
        } else {
            null
        }
    } catch (e: Exception) {
        Logger.e(e) { "Error getting file size: $filePath" }
        null
    }
}