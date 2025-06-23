package com.amaurysdelossantos.project.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.amaurysdelossantos.project.database.enums.ReadingDirection
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "comic_pages",
    foreignKeys = [ForeignKey(
        entity = Chapter::class,
        parentColumns = ["id"],
        childColumns = ["chapterId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("chapterId"), Index("pageNumber")]
)
data class ComicPage @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val chapterId: String,
    val bookId: String, // Denormalized for easier querying
    val imageUrl: String? = null,
    val filePath: String? = null,
    val pageNumber: Int,
    val width: Int? = null,
    val height: Int? = null,
    val isDoublePage: Boolean = false,
    val readingDirection: ReadingDirection = ReadingDirection.LEFT_TO_RIGHT,
    val altText: String? = null,
    val translatorNotes: String? = null,

    // Sync fields
    var version: Int = 1,
    var lastSyncedAt: Long? = null,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    var updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    var isSynced: Boolean = false,

    var isDeleted: Boolean = false
)