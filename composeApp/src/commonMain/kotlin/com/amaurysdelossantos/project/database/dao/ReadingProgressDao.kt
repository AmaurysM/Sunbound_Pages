package com.amaurysdelossantos.project.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amaurysdelossantos.project.model.ReadingProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingProgressDao {
    @Query("SELECT * FROM reading_progress WHERE bookId = :bookId AND isDeleted = 0")
    suspend fun getProgressByBookId(bookId: String): ReadingProgress?

    @Query("SELECT * FROM reading_progress WHERE bookId = :bookId AND isDeleted = 0")
    fun getProgressByBookIdFlow(bookId: String): Flow<ReadingProgress?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: ReadingProgress)

    @Update
    suspend fun updateProgress(progress: ReadingProgress)

    @Query("DELETE FROM reading_progress WHERE bookId = :bookId")
    suspend fun deleteProgressByBookId(bookId: String)

    // Update specific progress fields
    @Query(
        """
        UPDATE reading_progress 
        SET currentPage = :page, progressPercentage = :percentage, updatedAt = :timestamp, isSynced = 0 
        WHERE bookId = :bookId
    """
    )
    suspend fun updatePageProgress(bookId: String, page: Int, percentage: Float, timestamp: Long)

    @Query(
        """
        UPDATE reading_progress 
        SET currentTrackTime = :time, progressPercentage = :percentage, updatedAt = :timestamp, isSynced = 0 
        WHERE bookId = :bookId
    """
    )
    suspend fun updateAudioProgress(bookId: String, time: Long, percentage: Float, timestamp: Long)

    @Query(
        """
        UPDATE reading_progress 
        SET currentEpisode = :episode, scrollPosition = :position, updatedAt = :timestamp, isSynced = 0 
        WHERE bookId = :bookId
    """
    )
    suspend fun updateWebtoonProgress(
        bookId: String,
        episode: Int,
        position: Float,
        timestamp: Long
    )

    // Reading session tracking
    @Query(
        """
        UPDATE reading_progress 
        SET totalReadingTimeMinutes = totalReadingTimeMinutes + :minutes, 
            sessionsCount = sessionsCount + 1,
            lastSessionDuration = :minutes,
            updatedAt = :timestamp,
            isSynced = 0
        WHERE bookId = :bookId
    """
    )
    suspend fun addReadingSession(bookId: String, minutes: Int, timestamp: Long)

    // Sync operations
    @Query("SELECT * FROM reading_progress WHERE isSynced = 0 OR updatedAt > COALESCE(lastSyncedAt, 0)")
    suspend fun getUnsyncedProgress(): List<ReadingProgress>

    @Query("UPDATE reading_progress SET isSynced = 1, lastSyncedAt = :timestamp WHERE id = :id")
    suspend fun markProgressAsSynced(id: String, timestamp: Long)
}