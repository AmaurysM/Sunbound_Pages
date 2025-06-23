package com.amaurysdelossantos.project.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.model.BookTagCrossRef
import com.amaurysdelossantos.project.model.Tag
import com.amaurysdelossantos.project.model.complexRelations.BookWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Query("SELECT * FROM tags WHERE isDeleted = 0 ORDER BY name ASC")
    fun getAllTags(): Flow<List<Tag>>

    @Query("SELECT * FROM tags WHERE id = :tagId AND isDeleted = 0")
    suspend fun getTagById(tagId: String): Tag?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBookTag(bookTag: BookTagCrossRef)

    @Query("DELETE FROM book_tags WHERE bookId = :bookId AND tagId = :tagId")
    suspend fun removeBookTag(bookId: String, tagId: String)

    @Transaction
    @Query("SELECT * FROM books WHERE id = :bookId AND isDeleted = 0")
    suspend fun getBookWithTags(bookId: String): BookWithTags?

    @Query(
        """
        SELECT t.* FROM tags t 
        INNER JOIN book_tags bt ON t.id = bt.tagId 
        WHERE bt.bookId = :bookId AND t.isDeleted = 0
        ORDER BY t.name ASC
    """
    )
    fun getTagsByBookId(bookId: String): Flow<List<Tag>>

    @Query(
        """
        SELECT b.* FROM books b 
        INNER JOIN book_tags bt ON b.id = bt.bookId 
        WHERE bt.tagId = :tagId AND b.isDeleted = 0
        ORDER BY b.title ASC
    """
    )
    fun getBooksByTagId(tagId: String): Flow<List<Book>>
}
