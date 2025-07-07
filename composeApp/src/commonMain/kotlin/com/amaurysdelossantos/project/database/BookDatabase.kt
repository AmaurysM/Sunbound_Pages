package com.amaurysdelossantos.project.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.amaurysdelossantos.project.database.dao.AnnotationDao
import com.amaurysdelossantos.project.database.dao.AudioTrackDao
import com.amaurysdelossantos.project.database.dao.BookDao
import com.amaurysdelossantos.project.database.dao.ChapterDao
import com.amaurysdelossantos.project.database.dao.ComicDao
import com.amaurysdelossantos.project.database.dao.ReadingProgressDao
import com.amaurysdelossantos.project.database.dao.SyncDao
import com.amaurysdelossantos.project.database.dao.TagDao
import com.amaurysdelossantos.project.database.dao.WebtoonDao
import com.amaurysdelossantos.project.model.AudioTrack
import com.amaurysdelossantos.project.model.Book
import com.amaurysdelossantos.project.model.BookTagCrossRef
import com.amaurysdelossantos.project.model.Bookmark
import com.amaurysdelossantos.project.model.Chapter
import com.amaurysdelossantos.project.model.ComicPage
import com.amaurysdelossantos.project.model.Highlight
import com.amaurysdelossantos.project.model.ReadingProgress
import com.amaurysdelossantos.project.model.SyncMetadata
import com.amaurysdelossantos.project.model.Tag
import com.amaurysdelossantos.project.model.WebtoonEpisode
import com.amaurysdelossantos.project.model.WebtoonPanel

@Database(
    entities = [
        Book::class,
        ReadingProgress::class,
        Chapter::class,
        AudioTrack::class,
        WebtoonEpisode::class,
        WebtoonPanel::class,
        ComicPage::class,
        Highlight::class,
        Bookmark::class,
        Tag::class,
        BookTagCrossRef::class,
        SyncMetadata::class
    ],
    version = 5,
    exportSchema = true
)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(BookConverters::class)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun readingProgressDao(): ReadingProgressDao
    abstract fun chapterDao(): ChapterDao
    abstract fun audioTrackDao(): AudioTrackDao
    abstract fun webtoonDao(): WebtoonDao
    abstract fun comicDao(): ComicDao
    abstract fun annotationDao(): AnnotationDao
    abstract fun tagDao(): TagDao
    abstract fun syncDao(): SyncDao

    companion object {
        const val DATABASE_NAME = "book_database.db"
    }
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<BookDatabase> {
    override fun initialize(): BookDatabase
}
