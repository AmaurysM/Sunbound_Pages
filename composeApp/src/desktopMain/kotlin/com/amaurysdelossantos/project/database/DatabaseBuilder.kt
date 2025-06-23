package com.amaurysdelossantos.project.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File


fun getDatabaseBuilder(): RoomDatabase.Builder<BookDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "sun.db")
    return Room.databaseBuilder<BookDatabase>(
        name = dbFile.absolutePath,
    )
}

//fun getDatabaseBuilder(): BookDatabase {
//    val dbFilePath = getDatabasePath()
//    return Room.databaseBuilder<BookDatabase>(
//        name = dbFilePath,
//
//        )
//        .addMigrations(/* Add migrations here as needed */)
//        // .fallbackToDestructiveMigration() // Remove in production
//        .build()
//}
//
//private fun getDatabasePath(): String {
//    // Get the user's home directory
//    val userHome = System.getProperty("user.home")
//
//    // Create app-specific directory in user's home
//    val appDir = File(userHome, ".sunbound") // You can customize this name
//
//    // Ensure the directory exists
//    if (!appDir.exists()) {
//        appDir.mkdirs()
//    }
//
//    // Return the full path to the database file
//    return File(appDir, BookDatabase.DATABASE_NAME).absolutePath
//}