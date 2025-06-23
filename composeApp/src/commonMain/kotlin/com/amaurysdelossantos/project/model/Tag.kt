package com.amaurysdelossantos.project.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "tags")
data class Tag @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    val name: String,
    val color: String? = null,

    // Sync fields
    var version: Int = 1,
    var lastSyncedAt: Long? = null,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    var updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    var isSynced: Boolean = false,
    var isDeleted: Boolean = false
)
