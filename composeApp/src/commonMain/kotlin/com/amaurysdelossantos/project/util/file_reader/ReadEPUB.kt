package com.amaurysdelossantos.project.util.file_reader

data class EpubChapter(
    val title: String?,
    val htmlContent: String
)

data class EpubBook(
    val title: String?,
    val author: String?,
    val coverImageBytes: ByteArray?, // Could be null
    val chapters: List<EpubChapter>
)

expect suspend fun ReadEpub(path: String): EpubBook?
