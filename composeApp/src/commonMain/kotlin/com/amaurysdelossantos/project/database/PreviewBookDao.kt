package com.amaurysdelossantos.project.database

import com.amaurysdelossantos.project.database.dao.BookDao
import com.amaurysdelossantos.project.database.enums.BookFormat
import com.amaurysdelossantos.project.database.enums.MediaType
import com.amaurysdelossantos.project.database.enums.ReadingStatus
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.model.MediaTypeCount
import com.amaurysdelossantos.project.model.complexRelations.BookWithProgress
import com.amaurysdelossantos.project.model.complexRelations.CompleteBook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock

class PreviewBookDao : BookDao {
    private val sampleBooks = mutableMapOf(
        "preview-book-1" to Book(
            id = "preview-book-1",
            title = "The Great Gatsby",
            author = "F. Scott Fitzgerald",
            description = "A classic American novel set in the Jazz Age, following the mysterious Jay Gatsby and his obsession with Daisy Buchanan.",
            genre = "Classic Literature",
            language = "English",
            publicationYear = 1925,
            publisher = "Charles Scribner's Sons",
            totalPages = 180,
            estimatedReadingTimeMinutes = 240,
            rating = 0.7f,
            mediaType = MediaType.COMIC,
            coverImageUrl = "",
            format = BookFormat.EPUB,
            filePath = "",
            fileSize = 1_024_000,
            checksum = "abc123",
            totalDuration = null,
            readingStatus = ReadingStatus.COMPLETED,
            isFavorite = true,
            personalNotes = "Loved the writing style.",
            serverBookId = "srv001",
            version = 1,
            lastSyncedAt = Clock.System.now().toEpochMilliseconds(),
            createdAt = Clock.System.now().toEpochMilliseconds(),
            updatedAt = Clock.System.now().toEpochMilliseconds(),
            isSynced = true,
            isDeleted = false,
        ),
        "preview-book-2" to Book(
            id = "preview-book-2",
            title = "1984",
            author = "George Orwell",
            description = "A dystopian social science fiction novel that follows Winston Smith, a low-ranking member of 'the Party'.",
            genre = "Dystopian Fiction",
            language = "English",
            publicationYear = 1949,
            publisher = "Secker & Warburg",
            totalPages = 328,
            estimatedReadingTimeMinutes = 480,
            rating = 4.6f,
            mediaType = MediaType.EBOOK,
            coverImageUrl = "https://example.com/1984.jpg",
            format = BookFormat.PDF,
            filePath = "/books/1984.pdf",
            fileSize = 2_048_000,
            checksum = "def456",
            totalDuration = null,
            readingStatus = ReadingStatus.READING,
            isFavorite = false,
            personalNotes = "Terrifyingly realistic.",
            serverBookId = "srv002",
            version = 1,
            lastSyncedAt = Clock.System.now().toEpochMilliseconds(),
            createdAt = Clock.System.now().toEpochMilliseconds(),
            updatedAt = Clock.System.now().toEpochMilliseconds(),
            isSynced = true,
            isDeleted = false,
        ),
        "preview-book-3" to Book(
            id = "preview-book-3",
            title = "To Kill a Mockingbird",
            author = "Harper Lee",
            description = "A novel of warmth and humor despite dealing with serious issues of rape and racial inequality.",
            genre = "Southern Gothic",
            language = "English",
            publicationYear = 1960,
            publisher = "J.B. Lippincott & Co.",
            totalPages = 281,
            estimatedReadingTimeMinutes = 360,
            rating = 4.9f,
            mediaType = MediaType.EBOOK,
            coverImageUrl = "https://example.com/mockingbird.jpg",
            format = BookFormat.MOBI,
            filePath = "/books/mockingbird.mobi",
            fileSize = 1_500_000,
            checksum = "ghi789",
            totalDuration = null,
            readingStatus = ReadingStatus.NOT_STARTED,
            isFavorite = true,
            personalNotes = "",
            serverBookId = "srv003",
            version = 1,
            lastSyncedAt = Clock.System.now().toEpochMilliseconds(),
            createdAt = Clock.System.now().toEpochMilliseconds(),
            updatedAt = Clock.System.now().toEpochMilliseconds(),
            isSynced = false,
            isDeleted = false,
        ),
        "preview-book-4" to Book(
            id = "preview-book-4",
            title = "The Hobbit",
            author = "J.R.R. Tolkien",
            description = "A fantasy novel about the quest of Bilbo Baggins, who is swept into an epic journey.",
            genre = "Fantasy",
            language = "English",
            publicationYear = 1937,
            publisher = "George Allen & Unwin",
            totalPages = 310,
            estimatedReadingTimeMinutes = 400,
            rating = 4.8f,
            mediaType = MediaType.AUDIOBOOK,
            coverImageUrl = "https://example.com/hobbit.jpg",
            format = BookFormat.MP3,
            filePath = "/audiobooks/hobbit.mp3",
            fileSize = 30_000_000,
            checksum = "jkl012",
            totalDuration = 19800, // 5.5 hours
            readingStatus = ReadingStatus.READING,
            isFavorite = false,
            personalNotes = "Currently listening on commute.",
            serverBookId = "srv004",
            version = 1,
            lastSyncedAt = Clock.System.now().toEpochMilliseconds(),
            createdAt = Clock.System.now().toEpochMilliseconds(),
            updatedAt = Clock.System.now().toEpochMilliseconds(),
            isSynced = true,
            isDeleted = false,
        ),
        "preview-book-5" to Book(
            id = "preview-book-5",
            title = "Attack on Titan Vol. 1",
            author = "Hajime Isayama",
            description = "In a world where giant humanoids threaten humanity, Eren and his friends fight for survival.",
            genre = "Action, Dark Fantasy",
            language = "Japanese",
            publicationYear = 2009,
            publisher = "Kodansha",
            totalPages = 192,
            estimatedReadingTimeMinutes = 90,
            rating = 4.3f,
            mediaType = MediaType.MANGA,
            coverImageUrl = "https://example.com/aot1.jpg",
            format = BookFormat.CBZ,
            filePath = "/manga/aot1.cbz",
            fileSize = 55_000_000,
            checksum = "mno345",
            totalDuration = null,
            readingStatus = ReadingStatus.PLAN_TO_READ,
            isFavorite = false,
            personalNotes = "",
            serverBookId = "srv005",
            version = 1,
            lastSyncedAt = Clock.System.now().toEpochMilliseconds(),
            createdAt = Clock.System.now().toEpochMilliseconds(),
            updatedAt = Clock.System.now().toEpochMilliseconds(),
            isSynced = false,
            isDeleted = false,
        ),
        "preview-book-6" to Book(
            id = "preview-book-6",
            title = "Webtoon Sampler",
            author = "Various",
            description = "A collection of short webtoons across genres: romance, horror, comedy, and action.",
            genre = "Anthology",
            language = "English",
            publicationYear = 2023,
            publisher = "Webtoon Originals",
            totalPages = 120,
            estimatedReadingTimeMinutes = 80,
            rating = 3.9f,
            mediaType = MediaType.WEBTOON,
            coverImageUrl = "https://example.com/webtoon.jpg",
            format = BookFormat.WEBTOON,
            filePath = "/webtoons/sampler.webtoon",
            fileSize = 10_000_000,
            checksum = "pqr678",
            totalDuration = null,
            readingStatus = ReadingStatus.ON_HOLD,
            isFavorite = true,
            personalNotes = "Need to finish chapter 5.",
            serverBookId = "srv006",
            version = 1,
            lastSyncedAt = Clock.System.now().toEpochMilliseconds(),
            createdAt = Clock.System.now().toEpochMilliseconds(),
            updatedAt = Clock.System.now().toEpochMilliseconds(),
            isSynced = true,
            isDeleted = false,
        )
    )

    // Basic CRUD operations
    override fun getAllBooks(): Flow<List<Book>> =
        flowOf(sampleBooks.values.filter { !it.isDeleted }.sortedByDescending { it.updatedAt })

    override suspend fun getBookById(bookId: String): Book? =
        sampleBooks[bookId]?.takeIf { !it.isDeleted }

    override fun getBookByIdFlow(bookId: String): Flow<Book?> =
        flowOf(sampleBooks[bookId]?.takeIf { !it.isDeleted })

    override suspend fun insertBook(book: Book) {
        sampleBooks[book.id] = book
    }

    override suspend fun insertBooks(books: List<Book>) {
        books.forEach { sampleBooks[it.id] = it }
    }

    override suspend fun updateBook(book: Book) {
        sampleBooks[book.id] = book.copy(updatedAt = Clock.System.now().toEpochMilliseconds())
    }

    override suspend fun softDeleteBook(bookId: String, timestamp: Long) {
        sampleBooks[bookId]?.let { book ->
            sampleBooks[bookId] = book.copy(isDeleted = true, updatedAt = timestamp)
        }
    }

    override suspend fun hardDeleteBook(book: Book) {
        sampleBooks.remove(book.id)
    }

    // Complex queries with relations (simplified for preview)
    override suspend fun getCompleteBook(bookId: String): CompleteBook? {
        val book = getBookById(bookId) ?: return null
        // Since CompleteBook includes relations, we'll create a mock implementation
        // You'll need to adapt this based on your actual CompleteBook structure
        return CompleteBook(
            book = book,
            progress = TODO(),
            chapters = TODO(),
            audioTracks = TODO(),
            webtoonEpisodes = TODO(),
            highlights = TODO(),
            bookmarks = TODO(),
            tags = TODO(),
            // Add other related data as needed
        )
    }

    override fun getAllCompleteBooks(): Flow<List<CompleteBook>> {
        return flowOf(
            sampleBooks.values
                .filter { !it.isDeleted }
                .sortedByDescending { it.updatedAt }
                .map { book ->
                    CompleteBook(
                        book = book,
                        progress = TODO(),
                        chapters = TODO(),
                        audioTracks = TODO(),
                        webtoonEpisodes = TODO(),
                        highlights = TODO(),
                        bookmarks = TODO(),
                        tags = TODO(),
                        // Add other related data as needed
                    )
                }
        )
    }

    override suspend fun getBookWithProgress(bookId: String): BookWithProgress? {
        val book = getBookById(bookId) ?: return null
        // Mock BookWithProgress - adapt based on your actual structure
        return BookWithProgress(
            book = book,
            progress = TODO(),
            // Add progress data as needed
        )
    }

    override fun getBooksByMediaType(mediaType: MediaType): Flow<List<BookWithProgress>> {
        return flowOf(
            sampleBooks.values
                .filter { !it.isDeleted && it.mediaType == mediaType }
                .sortedBy { it.title }
                .map { book ->
                    BookWithProgress(
                        book = book,
                        progress = TODO(),
                    )
                }
        )
    }

    // Search and filtering
    override fun searchBooks(searchQuery: String): Flow<List<Book>> {
        return flowOf(
            sampleBooks.values
                .filter { book ->
                    !book.isDeleted &&
                            (book.title.contains(searchQuery, ignoreCase = true) ||
                                    book.author?.contains(searchQuery, ignoreCase = true) == true)
                }
                .sortedBy { it.title }
        )
    }

    override fun getBooksByStatus(status: ReadingStatus): Flow<List<Book>> {
        return flowOf(
            sampleBooks.values
                .filter { !it.isDeleted && it.readingStatus == status }
                .sortedByDescending { it.updatedAt }
        )
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return flowOf(
            sampleBooks.values
                .filter { !it.isDeleted && it.isFavorite }
                .sortedByDescending { it.updatedAt }
        )
    }

    override fun getBooksByGenre(genre: String): Flow<List<Book>> {
        return flowOf(
            sampleBooks.values
                .filter { !it.isDeleted && it.genre == genre }
                .sortedBy { it.title }
        )
    }

    // Sync-related queries
    override suspend fun getUnsyncedBooks(): List<Book> {
        return sampleBooks.values.filter { book ->
            !book.isSynced || book.updatedAt > (book.lastSyncedAt ?: 0)
        }
    }

    override suspend fun markBookAsSynced(bookId: String, timestamp: Long) {
        sampleBooks[bookId]?.let { book ->
            sampleBooks[bookId] = book.copy(isSynced = true, lastSyncedAt = timestamp)
        }
    }

    override suspend fun incrementBookVersion(bookId: String, timestamp: Long) {
        sampleBooks[bookId]?.let { book ->
            sampleBooks[bookId] = book.copy(version = book.version + 1, updatedAt = timestamp)
        }
    }

    // Statistics
    override fun getTotalBooksCount(): Flow<Int> {
        return flowOf(sampleBooks.values.count { !it.isDeleted })
    }

    override fun getBookCountByStatus(status: ReadingStatus): Flow<Int> {
        return flowOf(sampleBooks.values.count { !it.isDeleted && it.readingStatus == status })
    }

    override fun getBookCountsByMediaType(): Flow<List<MediaTypeCount>> {
        return flowOf(
            sampleBooks.values
                .filter { !it.isDeleted }
                .groupBy { it.mediaType }
                .map { (mediaType, books) ->
                    MediaTypeCount(mediaType = mediaType, count = books.size)
                }
        )
    }

    override fun getCurrentlyReadingBooks(): Flow<List<Book>> {
        return getBooksByStatus(ReadingStatus.READING)
    }

    override fun getRecentlyOpenedBooks(): Flow<List<Book>> {
        return flowOf(
            sampleBooks.values
                .filter { !it.isDeleted && it.readingStatus == ReadingStatus.READING }
                .sortedByDescending { it.updatedAt }
        )
    }

    override fun getFinishedBooks(): Flow<List<Book>> {
        return getBooksByStatus(ReadingStatus.COMPLETED)
    }
}