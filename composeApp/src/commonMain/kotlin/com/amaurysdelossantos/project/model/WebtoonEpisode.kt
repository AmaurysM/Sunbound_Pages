package com.amaurysdelossantos.project.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "webtoon_episodes",
    foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = ["id"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("bookId"), Index("episodeNumber"), Index("publishDate")]
)
data class WebtoonEpisode @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val bookId: String,
    val title: String,
    val episodeNumber: Int,
    val seasonNumber: Int? = null,
    val publishDate: String? = null, // When episode was published online
    val thumbnailUrl: String? = null,

    // Webtoon-specific monetization features
    val isFree: Boolean = true,
    val unlockDate: String? = null, // For "fast pass" or timed releases
    val coinCost: Int? = null, // Cost in platform currency
    val isLocked: Boolean = false,

    // Content metadata
    val contentWarning: String? = null, // Age rating, trigger warnings
    val authorNotes: String? = null, // Creator's notes for the episode
    val totalHeight: Int? = null, // Total vertical height in pixels
    val estimatedReadTime: Int? = null, // Estimated reading time in minutes
    val likeCount: Int? = null, // Social engagement metrics
    val commentCount: Int? = null,

    // Sync fields
    var version: Int = 1,
    var lastSyncedAt: Long? = null,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    var updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    var isSynced: Boolean = false,
    var isDeleted: Boolean = false
)