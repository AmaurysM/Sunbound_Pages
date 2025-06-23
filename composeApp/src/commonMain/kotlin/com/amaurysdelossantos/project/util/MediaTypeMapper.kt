package com.amaurysdelossantos.project.util

import com.amaurysdelossantos.project.database.enums.BookFormat
import com.amaurysdelossantos.project.database.enums.MediaType

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
