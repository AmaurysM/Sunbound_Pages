package com.amaurysdelossantos.project.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.amaurysdelossantos.project.model.Chapter
import com.amaurysdelossantos.project.model.complexRelations.ChapterWithPages
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {

    @Query("SELECT * FROM chapters WHERE bookId = :bookId AND isDeleted = 0 ORDER BY chapterNumber ASC")
    fun getChaptersByBookId(bookId: String): Flow<List<Chapter>>

    @Query("SELECT * FROM chapters WHERE id = :chapterId AND isDeleted = 0")
    suspend fun getChapterById(chapterId: String): Chapter?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: Chapter)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<Chapter>)

    @Update
    suspend fun updateChapter(chapter: Chapter)

    @Query("UPDATE chapters SET isDeleted = 1, updatedAt = :timestamp WHERE id = :chapterId")
    suspend fun softDeleteChapter(chapterId: String, timestamp: Long)

    @Transaction
    @Query("SELECT * FROM chapters WHERE id = :chapterId AND isDeleted = 0")
    suspend fun getChapterWithPages(chapterId: String): ChapterWithPages?

    @Query("SELECT COUNT(*) FROM chapters WHERE bookId = :bookId AND isDeleted = 0")
    suspend fun getChapterCountByBookId(bookId: String): Int
}