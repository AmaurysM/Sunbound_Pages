package com.amaurysdelossantos.project.model.complexRelations

import androidx.room.Embedded
import androidx.room.Relation
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.model.Chapter

data class BookWithChapters(
    @Embedded val book: Book,
    @Relation(
        parentColumn = "id",
        entityColumn = "bookId"
    )
    val chapters: List<Chapter>
)