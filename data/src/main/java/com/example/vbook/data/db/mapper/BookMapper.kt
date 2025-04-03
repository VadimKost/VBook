/*
 * Created by Vadim on 21.07.22, 23:06
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 13.07.22, 12:19
 *
 */

package com.example.vbook.data.db.mapper

import com.example.vbook.data.db.model.book.AuthorEntity
import com.example.vbook.data.db.model.book.BookEntity
import com.example.vbook.data.db.model.book.CycleEntity
import com.example.vbook.data.db.model.book.DetailedBook
import com.example.vbook.data.db.model.book.DetailedVoiceover
import com.example.vbook.data.db.model.book.MediaItemEntity
import com.example.vbook.data.db.model.book.ReaderEntity
import com.example.vbook.data.db.model.book.VoiceoverEntity
import com.example.vbook.domain.book.model.Author
import com.example.vbook.domain.book.model.Book
import com.example.vbook.domain.book.model.Cycle
import com.example.vbook.domain.book.model.Reader
import com.example.vbook.domain.book.model.Voiceover
import com.example.vbook.domain.shared.model.MediaItem
import java.util.UUID

object BookMapper {

    // ─────────────────── Book Mappers ───────────────────

    fun Book.toEntity(): DetailedBook = DetailedBook(
        book = BookEntity(
            inAppId = this.inAppId.ifEmpty { UUID.randomUUID().toString() },
            title = this.title,
            cover = this.cover,
            cycle = this.cycle?.toEntity(),
        ),
        authors = this.authors.map { it.toEntity() },
        voiceovers = this.voiceovers.map { it.toEntity() },
        externalIds =  listOf()
    )

    fun DetailedBook.toDomain(): Book = Book(
        inAppId = this.book.inAppId,
        title = this.book.title,
        cover = this.book.cover,
        authors = this.authors.map { it.toDomain() },
        voiceovers = this.voiceovers.map { it.toDomain() },
        cycle = this.book.cycle?.toDomain()
    )


    // ─────────────────── Author Mappers ───────────────────

    fun Author.toEntity(): AuthorEntity = AuthorEntity(
        fullName = this.fullName
    )

    fun AuthorEntity.toDomain(): Author = Author(
        fullName = this.fullName
    )

    // ─────────────────── Cycle Mappers ────────────────────

    fun Cycle.toEntity(): CycleEntity = CycleEntity(
        name = this.name,
        numberInCycle = this.numberInCycle
    )

    fun CycleEntity.toDomain(): Cycle = Cycle(
        name = this.name,
        numberInCycle = this.numberInCycle
    )

    // ─────────────────── Voiceover Mappers ───────────────────

    fun Voiceover.toEntity(): DetailedVoiceover = DetailedVoiceover(
        voiceoverEntity = VoiceoverEntity(),
        readers = this.readers.map { it.toEntity() },
        mediaItems = this.mediaItems.map { it.toEntity() }
    )

    fun DetailedVoiceover.toDomain(): Voiceover = Voiceover(
        readers = this.readers.map { it.toDomain() },
        mediaItems = this.mediaItems.map { it.toDomain() }
    )

    // ─────────────────── Reader Mappers ───────────────────

    fun Reader.toEntity(): ReaderEntity = ReaderEntity(
        fullName = this.fullName
    )

    fun ReaderEntity.toDomain(): Reader = Reader(
        fullName = this.fullName
    )

    // ─────────────────── MediaItem Mappers ───────────────────

    fun MediaItem.toEntity(): MediaItemEntity = MediaItemEntity(
        url = this.uri,
        title = this.title,
        duration = this.duration
    )

    fun MediaItemEntity.toDomain(): MediaItem = MediaItem(
        uri = this.url,
        title = this.title,
        duration = this.duration
    )

}

