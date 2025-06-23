package com.amaurysdelossantos.project.database

import androidx.room.TypeConverter
import com.amaurysdelossantos.project.database.enums.BookFormat
import com.amaurysdelossantos.project.database.enums.HighlightColor
import com.amaurysdelossantos.project.database.enums.MediaType
import com.amaurysdelossantos.project.database.enums.ReadingDirection
import com.amaurysdelossantos.project.database.enums.ReadingStatus
import com.amaurysdelossantos.project.database.enums.SyncOperation
import com.amaurysdelossantos.project.database.enums.WebtoonPanelType

class BookConverters {

    @TypeConverter
    fun fromBookFormat(format: BookFormat): String = format.name

    @TypeConverter
    fun toBookFormat(format: String): BookFormat = BookFormat.valueOf(format)

    @TypeConverter
    fun fromMediaType(mediaType: MediaType): String = mediaType.name

    @TypeConverter
    fun toMediaType(mediaType: String): MediaType = MediaType.valueOf(mediaType)

    @TypeConverter
    fun fromReadingStatus(status: ReadingStatus): String = status.name

    @TypeConverter
    fun toReadingStatus(status: String): ReadingStatus = ReadingStatus.valueOf(status)

    @TypeConverter
    fun fromReadingDirection(direction: ReadingDirection): String = direction.name

    @TypeConverter
    fun toReadingDirection(direction: String): ReadingDirection =
        ReadingDirection.valueOf(direction)

    @TypeConverter
    fun fromHighlightColor(color: HighlightColor): String = color.name

    @TypeConverter
    fun toHighlightColor(color: String): HighlightColor = HighlightColor.valueOf(color)

    @TypeConverter
    fun fromWebtoonPanelType(type: WebtoonPanelType): String = type.name

    @TypeConverter
    fun toWebtoonPanelType(type: String): WebtoonPanelType = WebtoonPanelType.valueOf(type)

    @TypeConverter
    fun fromSyncOperation(operation: SyncOperation): String = operation.name

    @TypeConverter
    fun toSyncOperation(operation: String): SyncOperation = SyncOperation.valueOf(operation)
}
