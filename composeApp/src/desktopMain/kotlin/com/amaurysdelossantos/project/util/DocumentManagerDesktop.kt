package com.amaurysdelossantos.project.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.amaurysdelossantos.project.database.enums.BookFormat
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.nio.file.Files
import javax.swing.SwingUtilities

@Composable
actual fun rememberDocumentManager(onResult: (SharedDocument?) -> Unit): DocumentManager {
    // We create a DocumentManager with a launch lambda that opens the native file dialog
    return remember {
        DocumentManager(
            onLaunch = {
                // Because AWT FileDialog must be run on the UI thread, we use SwingUtilities.invokeLater
                SwingUtilities.invokeLater {
                    val dialog = FileDialog(Frame(), "Select Document", FileDialog.LOAD)
                    dialog.isVisible = true
                    val directory = dialog.directory
                    val file = dialog.file
                    if (directory != null && file != null) {
                        val selectedFile = File(directory, file)
                        onResult(SharedDocument(selectedFile))
                    } else {
                        onResult(null)
                    }
                }
            }
        )
    }
}

actual class DocumentManager actual constructor(private val onLaunch: () -> Unit) {
    actual fun launch() {
        onLaunch()
    }
}

actual class SharedDocument(private val file: File) {
    actual fun toByteArray(): ByteArray? {
        return try {
            Files.readAllBytes(file.toPath())
        } catch (e: Exception) {
            null
        }
    }

    actual fun bookFormat(): BookFormat? {
        return getBookFormat(file.name)
    }

    actual fun fileName(): String? {
        return file.name
    }
}