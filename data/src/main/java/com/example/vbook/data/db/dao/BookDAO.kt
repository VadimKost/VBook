/*
 * Created by Vadim on 13.07.22, 12:17
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 09.07.22, 20:35
 *
 */

package com.example.vbook.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.vbook.data.db.model.book.AuthorEntity
import com.example.vbook.data.db.model.book.BookEntity
import com.example.vbook.data.db.model.book.DetailedBook
import com.example.vbook.data.db.model.book.ExternalBookEntityId
import com.example.vbook.data.db.model.book.MediaItemEntity
import com.example.vbook.data.db.model.book.ReaderEntity
import com.example.vbook.data.db.model.book.VoiceoverEntity
import com.example.vbook.data.db.model.book.crossref.BookAuthorCrossRef
import com.example.vbook.data.db.model.book.crossref.VoiceoverReaderCrossRef

@Dao
interface BookDAO {

    // TODO: @Insert(onConflict = OnConflictStrategy.REPLACE) or @Upsert or query in my situation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBook(book: BookEntity)

    @Upsert
    suspend fun upsertAuthors(books: List<AuthorEntity>)

    @Upsert
    suspend fun upsertVoiceovers(voiceovers: List<VoiceoverEntity>)

    @Upsert
    suspend fun upsertReaders(readers: List<ReaderEntity>)

    @Upsert
    suspend fun upsertMediaItems(mediaItems: List<MediaItemEntity>)

    @Upsert
    suspend fun upsertBookAuthorCrossRefs(crossRef: List<BookAuthorCrossRef>)

    @Upsert
    suspend fun upsertVoiceoverReaderCrossRefs(crossRef: List<VoiceoverReaderCrossRef>)

    @Upsert
    suspend fun upsertExternalBookEntityIds(externalBookEntityIds: List<ExternalBookEntityId>)

    @Transaction
    @Query("SELECT * FROM book WHERE inAppId = :inAppId")
    fun getBookByInAppId(inAppId: String): DetailedBook

    // TODO: Optimize and refactor
    @Transaction
    suspend fun upsertDetailedBook(detailedBook: DetailedBook) {
        upsertBook(detailedBook.book)
        upsertAuthors(detailedBook.authors)
        upsertExternalBookEntityIds(detailedBook.externalIds)

        val bookAuthorCrossRef =
            detailedBook.authors.map {
                BookAuthorCrossRef(
                    bookId = detailedBook.book.inAppId,
                    authorId = it.id
                )
            }

        upsertBookAuthorCrossRefs(bookAuthorCrossRef)

        val voiceovers = detailedBook.voiceovers

        upsertVoiceovers(voiceovers.map { it.voiceoverEntity.copy(inAppBookId = detailedBook.book.inAppId) })

        voiceovers.forEach { voiceover ->
            upsertReaders(voiceover.readers)

            val voiceoverReaderCrossRef = voiceover.readers.map { reader ->
                VoiceoverReaderCrossRef(
                    readerId = reader.id,
                    voiceoverId = voiceover.voiceoverEntity.id,
                )
            }
            upsertVoiceoverReaderCrossRefs(voiceoverReaderCrossRef)

            upsertMediaItems(voiceover.mediaItems.map { it.copy(voiceoverId = voiceover.voiceoverEntity.id) })
        }


//        val bookId = insertBook(detailedBook.book)
//
//        val authorIds = insertAuthors(detailedBook.authors)
//
//        val bookAuthorCrossRef = authorIds.map { authorId -> BookAuthorCrossRef(bookId, authorId) }
//        insertBookAuthorCrossRef(bookAuthorCrossRef)
//
//        val voiceovers = detailedBook.voiceovers
//            .map { it.voiceoverEntity.copy(inAppBookId = bookId) }
//
//        val voiceoverIds = insertVoiceovers(voiceovers)
//
////        val readers = voiceovers.map { it. }
    }

}

