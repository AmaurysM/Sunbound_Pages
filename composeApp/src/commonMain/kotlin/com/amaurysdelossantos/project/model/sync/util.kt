package com.amaurysdelossantos.project.model.sync

import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.model.Highlight
import com.amaurysdelossantos.project.model.ReadingProgress

fun Book.needsSync(): Boolean = !isSynced || updatedAt > (lastSyncedAt ?: 0)
fun ReadingProgress.needsSync(): Boolean = !isSynced || updatedAt > (lastSyncedAt ?: 0)
fun Highlight.needsSync(): Boolean = !isSynced || updatedAt > (lastSyncedAt ?: 0)
