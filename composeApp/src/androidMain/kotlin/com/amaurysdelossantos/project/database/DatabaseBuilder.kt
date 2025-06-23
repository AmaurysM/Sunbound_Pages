package com.amaurysdelossantos.project.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<BookDatabase> {
    val dbFile = context.getDatabasePath("sun.db")
    val appContext = context.applicationContext

    return Room.databaseBuilder<BookDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}