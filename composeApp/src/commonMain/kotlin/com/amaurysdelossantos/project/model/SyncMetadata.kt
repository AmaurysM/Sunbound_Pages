package com.amaurysdelossantos.project.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amaurysdelossantos.project.database.enums.SyncOperation
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "sync_metadata")
data class SyncMetadata @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val entityType: String, // "book", "highlight", etc.
    val entityId: String,
    val operation: SyncOperation,
    val retryCount: Int = 0,
    val lastError: String? = null,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    var processedAt: Long? = null
)