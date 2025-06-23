package com.amaurysdelossantos.project.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amaurysdelossantos.project.model.AudioTrack
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioTrackDao {

    @Query("SELECT * FROM audio_tracks WHERE bookId = :bookId AND isDeleted = 0 ORDER BY trackNumber ASC")
    fun getAudioTracksByBookId(bookId: String): Flow<List<AudioTrack>>

    @Query("SELECT * FROM audio_tracks WHERE id = :trackId AND isDeleted = 0")
    suspend fun getAudioTrackById(trackId: String): AudioTrack?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudioTrack(track: AudioTrack)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudioTracks(tracks: List<AudioTrack>)

    @Update
    suspend fun updateAudioTrack(track: AudioTrack)

    @Query("UPDATE audio_tracks SET isDeleted = 1, updatedAt = :timestamp WHERE id = :trackId")
    suspend fun softDeleteAudioTrack(trackId: String, timestamp: Long)

    @Query("SELECT SUM(durationMillis) FROM audio_tracks WHERE bookId = :bookId AND isDeleted = 0")
    suspend fun getTotalDurationByBookId(bookId: String): Long?
}