package com.amaurysdelossantos.project.model.complexRelations

import androidx.room.Embedded
import androidx.room.Relation
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.model.ReadingProgress

data class BookWithProgress(
    @Embedded val book: Book,
    @Relation(
        parentColumn = "id",
        entityColumn = "bookId"
    )
    val progress: ReadingProgress?
)