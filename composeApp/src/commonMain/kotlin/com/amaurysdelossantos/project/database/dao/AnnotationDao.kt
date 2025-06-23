package com.amaurysdelossantos.project.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amaurysdelossantos.project.model.Bookmark
import com.amaurysdelossantos.project.model.Highlight
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnotationDao {

    // Highlights
    @Query("SELECT * FROM highlights WHERE bookId = :bookId AND isDeleted = 0 ORDER BY createdAt DESC")
    fun getHighlightsByBookId(bookId: String): Flow<List<Highlight>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighlight(highlight: Highlight)

    @Update
    suspend fun updateHighlight(highlight: Highlight)

    @Query("UPDATE highlights SET isDeleted = 1, updatedAt = :timestamp WHERE id = :highlightId")
    suspend fun softDeleteHighlight(highlightId: String, timestamp: Long)

    // Bookmarks
    @Query("SELECT * FROM bookmarks WHERE bookId = :bookId AND isDeleted = 0 ORDER BY createdAt DESC")
    fun getBookmarksByBookId(bookId: String): Flow<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark)

    @Update
    suspend fun updateBookmark(bookmark: Bookmark)

    @Query("UPDATE bookmarks SET isDeleted = 1, updatedAt = :timestamp WHERE id = :bookmarkId")
    suspend fun softDeleteBookmark(bookmarkId: String, timestamp: Long)

    // Search annotations
    @Query(
        """
        SELECT * FROM highlights 
        WHERE bookId = :bookId AND (text LIKE '%' || :query || '%' OR note LIKE '%' || :query || '%') 
        AND isDeleted = 0 
        ORDER BY createdAt DESC
    """
    )
    fun searchHighlights(bookId: String, query: String): Flow<List<Highlight>>
}