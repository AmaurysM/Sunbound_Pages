package com.amaurysdelossantos.project.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "audio_tracks",
    foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = ["id"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("bookId"), Index("trackNumber")]
)
data class AudioTrack @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val bookId: String,
    val title: String,
    val url: String? = null,
    val filePath: String? = null,
    val durationMillis: Long,
    val trackNumber: Int,
    val bitrate: Int? = null,
    val format: String? = null,
    val checksum: String? = null,

    // Sync fields
    var version: Int = 1,
    var lastSyncedAt: Long? = null,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    var updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    var isSynced: Boolean = false,
    var isDeleted: Boolean = false
)