package com.amaurysdelossantos.project.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "reading_progress",
    foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = ["id"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("bookId")]
)
data class ReadingProgress @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val bookId: String,

    // Universal progress
    var progressPercentage: Float = 0.0f,
    var currentPosition: String? = null, // JSON string for complex positions

    // Page-based progress
    var currentPage: Int? = null,
    var currentChapter: Int? = null,

    // Audio progress
    var currentTrackIndex: Int? = null,
    var currentTrackTime: Long? = null,

    // Comic progress
    var currentComicChapter: Int? = null,
    var currentComicPage: Int? = null,

    // Webtoon progress
    var currentEpisode: Int? = null,
    var scrollPosition: Float? = null,

    // Reading analytics
    var totalReadingTimeMinutes: Int = 0,
    var sessionsCount: Int = 0,
    var lastSessionStart: Long? = null,
    var lastSessionDuration: Int = 0,

    // Sync fields
    var version: Int = 1,
    var lastSyncedAt: Long? = null,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    var updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    var isSynced: Boolean = false,
    var isDeleted: Boolean = false
)