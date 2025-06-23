package com.amaurysdelossantos.project.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amaurysdelossantos.project.model.ComicPage
import kotlinx.coroutines.flow.Flow

@Dao
interface ComicDao {

    @Query("SELECT * FROM comic_pages WHERE chapterId = :chapterId AND isDeleted = 0 ORDER BY pageNumber ASC")
    fun getPagesByChapterId(chapterId: String): Flow<List<ComicPage>>

    @Query("SELECT * FROM comic_pages WHERE bookId = :bookId AND isDeleted = 0 ORDER BY pageNumber ASC")
    fun getPagesByBookId(bookId: String): Flow<List<ComicPage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPage(page: ComicPage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPages(pages: List<ComicPage>)

    @Query("SELECT COUNT(*) FROM comic_pages WHERE chapterId = :chapterId AND isDeleted = 0")
    suspend fun getPageCountByChapterId(chapterId: String): Int
}