package com.amaurysdelossantos.project.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.amaurysdelossantos.project.database.enums.MediaType
import com.amaurysdelossantos.project.database.enums.ReadingStatus
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.model.MediaTypeCount
import com.amaurysdelossantos.project.model.complexRelations.BookWithProgress
import com.amaurysdelossantos.project.model.complexRelations.CompleteBook
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    // Basic CRUD operations
    @Query("SELECT * FROM books WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE id = :bookId AND isDeleted = 0")
    suspend fun getBookById(bookId: String): Book?

    @Query("SELECT * FROM books WHERE id = :bookId AND isDeleted = 0")
    fun getBookByIdFlow(bookId: String): Flow<Book?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<Book>)

    @Update
    suspend fun updateBook(book: Book)

    @Query("UPDATE books SET isDeleted = 1, updatedAt = :timestamp WHERE id = :bookId")
    suspend fun softDeleteBook(bookId: String, timestamp: Long)

    //@Delete
    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun hardDeleteBook(bookId: String)

    // Complex queries with relations
    @Transaction
    @Query("SELECT * FROM books WHERE id = :bookId AND isDeleted = 0")
    suspend fun getCompleteBook(bookId: String): CompleteBook?

    @Transaction
    @Query("SELECT * FROM books WHERE isDeleted = 0 ORDER BY updatedAt DESC")
    fun getAllCompleteBooks(): Flow<List<CompleteBook>>

    @Transaction
    @Query("SELECT * FROM books WHERE id = :bookId AND isDeleted = 0")
    suspend fun getBookWithProgress(bookId: String): BookWithProgress?

    @Transaction
    @Query("SELECT * FROM books WHERE mediaType = :mediaType AND isDeleted = 0 ORDER BY title ASC")
    fun getBooksByMediaType(mediaType: MediaType): Flow<List<BookWithProgress>>

    // Search and filtering
    @Query(
        """
        SELECT * FROM books 
        WHERE (title LIKE '%' || :searchQuery || '%' OR author LIKE '%' || :searchQuery || '%') 
        AND isDeleted = 0 
        ORDER BY title ASC
    """
    )
    fun searchBooks(searchQuery: String): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE readingStatus = :status AND isDeleted = 0 ORDER BY updatedAt DESC")
    fun getBooksByStatus(status: ReadingStatus): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE isFavorite = 1 AND isDeleted = 0 ORDER BY updatedAt DESC")
    fun getFavoriteBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE genre = :genre AND isDeleted = 0 ORDER BY title ASC")
    fun getBooksByGenre(genre: String): Flow<List<Book>>

    // Sync-related queries
    @Query("SELECT * FROM books WHERE isSynced = 0 OR updatedAt > COALESCE(lastSyncedAt, 0)")
    suspend fun getUnsyncedBooks(): List<Book>

    @Query("UPDATE books SET isSynced = 1, lastSyncedAt = :timestamp WHERE id = :bookId")
    suspend fun markBookAsSynced(bookId: String, timestamp: Long)

    @Query("UPDATE books SET version = version + 1, updatedAt = :timestamp WHERE id = :bookId")
    suspend fun incrementBookVersion(bookId: String, timestamp: Long)

    // Statistics
    @Query("SELECT COUNT(*) FROM books WHERE isDeleted = 0")
    fun getTotalBooksCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM books WHERE readingStatus = :status AND isDeleted = 0")
    fun getBookCountByStatus(status: ReadingStatus): Flow<Int>

    @Query("SELECT mediaType, COUNT(*) as count FROM books WHERE isDeleted = 0 GROUP BY mediaType")
    fun getBookCountsByMediaType(): Flow<List<MediaTypeCount>>

    @Query("SELECT * FROM books WHERE readingStatus = 'READING'")
    fun getCurrentlyReadingBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE readingStatus = 'READING' ORDER BY updatedAt DESC")
    fun getRecentlyOpenedBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE readingStatus = 'COMPLETED'")
    fun getFinishedBooks(): Flow<List<Book>>

    @Query("UPDATE books SET lastReadPosition = :position WHERE id = :bookId")
    suspend fun updateBookLastReadPosition(bookId: String, position: Int)

}