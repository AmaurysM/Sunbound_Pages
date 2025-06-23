package com.amaurysdelossantos.project.model.sync

import com.amaurysdelossantos.project.database.enums.ConflictType

data class SyncConflict<T>(
    val local: T,
    val remote: T,
    val conflictType: ConflictType
)
