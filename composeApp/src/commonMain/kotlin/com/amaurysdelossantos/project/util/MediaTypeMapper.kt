package com.amaurysdelossantos.project.util

import com.amaurysdelossantos.project.database.enums.BookFormat
import com.amaurysdelossantos.project.database.enums.MediaType

fun getBookFormat(filename: String): BookFormat? {
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

fun BookFormat.toMediaType(): MediaType = when (this) {
    BookFormat.EPUB,
    BookFormat.PDF,
    BookFormat.MOBI,
    BookFormat.AZW,
    BookFormat.AZW3,
    BookFormat.TXT,
    BookFormat.RTF,
    BookFormat.DOC,
    BookFormat.DOCX,
    BookFormat.HTML,
    BookFormat.WEB_SERIAL ->
        MediaType.EBOOK

    BookFormat.CBZ,
    BookFormat.CBR,
    BookFormat.CB7,
    BookFormat.CBT,
    BookFormat.JPG,
    BookFormat.JPEG,
    BookFormat.PNG,
    BookFormat.GIF,
    BookFormat.WEBP,
    BookFormat.BMP ->
        MediaType.COMIC

    BookFormat.WEBTOON -> MediaType.WEBTOON

    BookFormat.MP3,
    BookFormat.AAC,
    BookFormat.M4A,
    BookFormat.M4B,
    BookFormat.OGG,
    BookFormat.FLAC,
    BookFormat.WAV ->
        MediaType.AUDIOBOOK
}
