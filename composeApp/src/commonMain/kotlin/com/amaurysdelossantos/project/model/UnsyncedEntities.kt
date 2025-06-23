package com.amaurysdelossantos.project.model

data class UnsyncedEntities(
    val books: List<Book>,
    val progress: List<ReadingProgress>,
    val highlights: List<Highlight>,
    val bookmarks: List<Bookmark>
)
