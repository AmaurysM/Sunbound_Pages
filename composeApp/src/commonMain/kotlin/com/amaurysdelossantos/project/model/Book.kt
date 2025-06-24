package com.amaurysdelossantos.project.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amaurysdelossantos.project.database.enums.BookFormat
import com.amaurysdelossantos.project.database.enums.MediaType
import com.amaurysdelossantos.project.database.enums.ReadingStatus
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// Main Book Entity - Core information only
@Entity(tableName = "books")
data class Book @OptIn(ExperimentalUuidApi::class) constructor(
    @PrimaryKey val id: String = Uuid.random().toString(),
    var title: String,
    var author: String? = null,
    var description: String? = null,
    var genre: String? = null,
    var language: String? = null,
    var publicationYear: Int? = null,
    var publisher: String? = null,
    var coverImageUrl: String? = null,
    var format: BookFormat,
    var mediaType: MediaType,

    // File information
    var filePath: String,
    var fileSize: Long? = null,
    var checksum: String? = null,

    // Basic metadata
    val estimatedReadingTimeMinutes: Int? = null,
    val totalPages: Int? = null,
    val totalDuration: Long? = null, // for audiobooks

    // User preferences and status
    var readingStatus: ReadingStatus = ReadingStatus.NOT_STARTED,
    var isFavorite: Boolean = false,
    var rating: Float? = null,
    val personalNotes: String? = null,

    // Sync and versioning
    val serverBookId: String? = null, // For server sync
    var version: Int = 1, // For conflict resolution
    var lastSyncedAt: Long? = null,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    var updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    var isSynced: Boolean = false,
    var isDeleted: Boolean = false // Soft delete for sync
)
//
//data class AudioTrack(
//    val title: String,
//    val url: String,
//    val durationMillis: Long
//)
//
//
//enum class BookFormat {
//    EPUB, PDF, MOBI, MP3, AAC, CBZ, CBR, IMAGE, WEBTOON
//}
//
//data class Chapter(
//    val title: String,
//    val content: String,   // Could be plain text, HTML, or styled content
//    val pageCount: Int? = null
//)
//
//enum class ReadingStatus {
//    NOT_STARTED, READING, COMPLETED, ABANDONED
//}
//
//data class ComicChapter(
//    val title: String,
//    val chapterNumber: Int,
//    val pages: List<Page>
//)
//
//data class Page(
//    val imageUrl: String,
//    val pageNumber: Int,
//    val isDoublePage: Boolean = false,
//    val altText: String? = null     // Optional accessibility or translator notes
//)
//
//enum class MediaType{
//    COMIC,
//    MANGE,
//    AUDIOBOOK,
//    EBOOK
//}
//
//data class Highlight(
//    val page: Int?,
//    val timestamp: Long,
//    val text: String?,
//    val note: String? = null
//)
//
