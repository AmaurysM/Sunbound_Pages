package com.amaurysdelossantos.project.util

import androidx.compose.runtime.Composable
import com.amaurysdelossantos.project.database.enums.BookFormat

@Composable
expect fun rememberDocumentManager(onResult: (SharedDocument?) -> Unit): DocumentManager

expect class DocumentManager(
    onLaunch: () -> Unit
) {
    fun launch()
}

expect class SharedDocument {
    fun toByteArray(): ByteArray?
    fun bookFormat(): BookFormat?
    fun fileName(): String?
}


