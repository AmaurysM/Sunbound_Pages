package com.amaurysdelossantos.project.model.complexRelations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.model.BookTagCrossRef
import com.amaurysdelossantos.project.model.Tag

data class BookWithTags(
    @Embedded val book: Book,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = BookTagCrossRef::class,
            parentColumn = "bookId",
            entityColumn = "tagId"
        )
    )
    val tags: List<Tag>
)

