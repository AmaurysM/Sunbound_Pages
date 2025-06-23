package com.amaurysdelossantos.project.model.complexRelations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.amaurysdelossantos.project.model.AudioTrack
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.model.BookTagCrossRef
import com.amaurysdelossantos.project.model.Bookmark
import com.amaurysdelossantos.project.model.Chapter
import com.amaurysdelossantos.project.model.Highlight
import com.amaurysdelossantos.project.model.ReadingProgress
import com.amaurysdelossantos.project.model.Tag
import com.amaurysdelossantos.project.model.WebtoonEpisode

data class CompleteBook(
    @Embedded val book: Book,
    @Relation(
        parentColumn = "id",
        entityColumn = "bookId"
    )
    val progress: ReadingProgress?,
    @Relation(
        parentColumn = "id",
        entityColumn = "bookId"
    )
    val chapters: List<Chapter>,
    @Relation(
        parentColumn = "id",
        entityColumn = "bookId"
    )
    val audioTracks: List<AudioTrack>,
    @Relation(
        parentColumn = "id",
        entityColumn = "bookId"
    )
    val webtoonEpisodes: List<WebtoonEpisode>,
    @Relation(
        parentColumn = "id",
        entityColumn = "bookId"
    )
    val highlights: List<Highlight>,
    @Relation(
        parentColumn = "id",
        entityColumn = "bookId"
    )
    val bookmarks: List<Bookmark>,
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
