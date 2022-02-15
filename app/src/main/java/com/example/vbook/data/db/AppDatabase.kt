package com.example.vbook.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vbook.data.db.dao.BookDAO
import com.example.vbook.data.db.dao.MediaItemDownloadDAO
import com.example.vbook.data.db.model.BookEntity
import com.example.vbook.data.db.model.DownloadingItemEntity

@Database(entities = [BookEntity::class, DownloadingItemEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDAO
    abstract fun mediaItemDownloadDAO(): MediaItemDownloadDAO
}