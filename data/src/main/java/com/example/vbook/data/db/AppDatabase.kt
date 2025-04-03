/*
 * Created by Vadim on 13.07.22, 12:17
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 09.07.22, 20:35
 *
 */

package com.example.vbook.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vbook.data.db.dao.BookDAO
import com.example.vbook.data.db.model.book.AuthorEntity
import com.example.vbook.data.db.model.book.BookEntity
import com.example.vbook.data.db.model.book.ExternalBookEntityId
import com.example.vbook.data.db.model.book.MediaItemEntity
import com.example.vbook.data.db.model.book.ReaderEntity
import com.example.vbook.data.db.model.book.VoiceoverEntity
import com.example.vbook.data.db.model.book.crossref.BookAuthorCrossRef
import com.example.vbook.data.db.model.book.crossref.VoiceoverReaderCrossRef

@Database(
    entities = [
        BookEntity::class, AuthorEntity::class, VoiceoverEntity::class,
        BookAuthorCrossRef::class, ReaderEntity::class, MediaItemEntity::class,
        VoiceoverReaderCrossRef::class, ExternalBookEntityId::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDAO
//    abstract fun mediaItemDownloadDAO(): MediaItemDownloadDAO
}