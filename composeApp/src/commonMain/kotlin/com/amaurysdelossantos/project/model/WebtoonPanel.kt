package com.amaurysdelossantos.project.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.amaurysdelossantos.project.database.enums.WebtoonPanelType
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "webtoon_panels",
    foreignKeys = [ForeignKey(
        entity = WebtoonEpisode::class,
        parentColumns = ["id"],
        childColumns = ["episodeId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("episodeId"), Index("order")]
)
data class WebtoonPanel @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val episodeId: String,
    val bookId: String, // Denormalized for easier querying
    val imageUrl: String? = null,
    val filePath: String? = null,
    val order: Int, // Order in the vertical strip
    val width: Int? = null,
    val height: Int? = null,
    val altText: String? = null,

    // Webtoon-specific properties
    val panelType: WebtoonPanelType = WebtoonPanelType.STANDARD,
    val hasAnimation: Boolean = false, // Some webtoons have animated panels
    val soundEffectText: String? = null, // Text for sound effects
    val backgroundColor: String? = null, // Some panels have specific backgrounds
    val isInteractive: Boolean = false, // For interactive webtoons

    // Positioning for vertical scroll
    val yPosition: Int? = null, // Y position in the full strip
    val isPageBreak: Boolean = false, // Natural pause points for readers

    // Sync fields
    var version: Int = 1,
    var lastSyncedAt: Long? = null,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    var updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    var isSynced: Boolean = false,
    var isDeleted: Boolean = false
)