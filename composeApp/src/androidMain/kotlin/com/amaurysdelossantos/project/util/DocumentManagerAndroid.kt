package com.amaurysdelossantos.project.util

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.amaurysdelossantos.project.database.enums.BookFormat

@Composable
actual fun rememberDocumentManager(onResult: (SharedDocument?) -> Unit): DocumentManager {
    val context = LocalContext.current
    val documentLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                onResult.invoke(SharedDocument(contentResolver = context.contentResolver, uri = it))
            }
        }
    return remember {
        DocumentManager(onLaunch = {
            documentLauncher.launch(arrayOf("*/*"))
        })
    }
}

actual class DocumentManager actual constructor(private val onLaunch: () -> Unit) {
    actual fun launch() {
        onLaunch()
    }
}

actual class SharedDocument(private val contentResolver: ContentResolver, private val uri: Uri) {
    actual fun toByteArray(): ByteArray? {
        return contentResolver.openInputStream(uri)?.readBytes()
    }

    actual fun bookFormat(): BookFormat? {
        val name = fileName()?.lowercase()
        return if (name == null) {
            null
        }else {
            GetBookFormat(name)
        }
    }

    actual fun fileName(): String? {
        var fileName: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }
}