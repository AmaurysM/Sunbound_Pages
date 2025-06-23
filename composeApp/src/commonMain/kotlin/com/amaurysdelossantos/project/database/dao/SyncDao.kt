package com.amaurysdelossantos.project.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.model.Bookmark
import com.amaurysdelossantos.project.model.Highlight
import com.amaurysdelossantos.project.model.ReadingProgress
import com.amaurysdelossantos.project.model.SyncMetadata
import com.amaurysdelossantos.project.model.UnsyncedEntities

@Dao
interface SyncDao {

    @Query("SELECT * FROM sync_metadata WHERE processedAt IS NULL ORDER BY createdAt ASC")
    suspend fun getPendingSyncOperations(): List<SyncMetadata>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncOperation(syncMeta: SyncMetadata)

    @Query("UPDATE sync_metadata SET processedAt = :timestamp WHERE id = :id")
    suspend fun markOperationAsProcessed(id: String, timestamp: Long)

    @Query("UPDATE sync_metadata SET retryCount = retryCount + 1, lastError = :error WHERE id = :id")
    suspend fun incrementRetryCount(id: String, error: String)

    @Query("DELETE FROM sync_metadata WHERE processedAt IS NOT NULL AND processedAt < :cutoffTime")
    suspend fun cleanupOldSyncOperations(cutoffTime: Long)

    // Get all entities that need syncing
    suspend fun getAllUnsyncedEntities(): UnsyncedEntities {
        return UnsyncedEntities(
            books = getUnsyncedBooks(),
            progress = getUnsyncedProgress(),
            highlights = getUnsyncedHighlights(),
            bookmarks = getUnsyncedBookmarks()
        )
    }

    @Query("SELECT * FROM books WHERE isSynced = 0 OR updatedAt > COALESCE(lastSyncedAt, 0)")
    suspend fun getUnsyncedBooks(): List<Book>

    @Query("SELECT * FROM reading_progress WHERE isSynced = 0 OR updatedAt > COALESCE(lastSyncedAt, 0)")
    suspend fun getUnsyncedProgress(): List<ReadingProgress>

    @Query("SELECT * FROM highlights WHERE isSynced = 0 OR updatedAt > COALESCE(lastSyncedAt, 0)")
    suspend fun getUnsyncedHighlights(): List<Highlight>

    @Query("SELECT * FROM bookmarks WHERE isSynced = 0 OR updatedAt > COALESCE(lastSyncedAt, 0)")
    suspend fun getUnsyncedBookmarks(): List<Bookmark>
}