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

fun GetBookFormat(filename: String): BookFormat? {
    val name = filename.lowercase()
    return when {
        name.endsWith(".epub") -> BookFormat.EPUB
        name.endsWith(".pdf") -> BookFormat.PDF
        name.endsWith(".mobi") -> BookFormat.MOBI
        name.endsWith(".azw") -> BookFormat.AZW
        name.endsWith(".azw3") -> BookFormat.AZW3
        name.endsWith(".txt") -> BookFormat.TXT
        name.endsWith(".rtf") -> BookFormat.RTF
        name.endsWith(".doc") -> BookFormat.DOC
        name.endsWith(".docx") -> BookFormat.DOCX
        name.endsWith(".mp3") -> BookFormat.MP3
        name.endsWith(".aac") -> BookFormat.AAC
        name.endsWith(".m4a") -> BookFormat.M4A
        name.endsWith(".m4b") -> BookFormat.M4B
        name.endsWith(".ogg") -> BookFormat.OGG
        name.endsWith(".flac") -> BookFormat.FLAC
        name.endsWith(".wav") -> BookFormat.WAV
        name.endsWith(".cbz") -> BookFormat.CBZ
        name.endsWith(".cbr") -> BookFormat.CBR
        name.endsWith(".cb7") -> BookFormat.CB7
        name.endsWith(".cbt") -> BookFormat.CBT
        name.endsWith(".jpg") || name.endsWith(".jpeg") -> BookFormat.JPG
        name.endsWith(".png") -> BookFormat.PNG
        name.endsWith(".gif") -> BookFormat.GIF
        name.endsWith(".webp") -> BookFormat.WEBP
        name.endsWith(".bmp") -> BookFormat.BMP
        name.endsWith(".html") -> BookFormat.HTML
        // You can use custom logic for WEBTOON, WEB_SERIAL if needed
        else -> null
    }
}
