package com.amaurysdelossantos.project.model.complexRelations

import androidx.room.Embedded
import androidx.room.Relation
import com.amaurysdelossantos.project.model.Chapter
import com.amaurysdelossantos.project.model.ComicPage

data class ChapterWithPages(
    @Embedded val chapter: Chapter,
    @Relation(
        parentColumn = "id",
        entityColumn = "chapterId"
    )
    val pages: List<ComicPage>
)
