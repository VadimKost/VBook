/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.data.db.model.book

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.vbook.data.db.model.book.crossref.BookAuthorCrossRef
import com.example.vbook.data.parsers.Source
import java.util.UUID

// TODO: Replace inAppId: String to UUID(room support)
// TODO: Remove ExternalBookEntityId because in fact is voiceoverID at site
@Entity(tableName = "Book")
data class BookEntity(

    @PrimaryKey
    @ColumnInfo(name = "inAppId")
    val inAppId: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "cover")
    val cover: String,

    @Embedded
    val cycle: CycleEntity?,

    val createdAt: Long = System.currentTimeMillis(),

    val modifiedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "ExternalBookEntityId",
    primaryKeys = ["inAppBookId", "source"]
)
data class ExternalBookEntityId(
    val inAppBookId: String,
    val source: Source,
    val externalId: String
)

data class DetailedBook(

    @Embedded
    val book: BookEntity,

    @Relation(
        parentColumn = "inAppId",
        entityColumn = "id",
        associateBy = Junction(
            value = BookAuthorCrossRef::class,
            parentColumn = "bookId",
            entityColumn = "authorId"
        )
    )
    val authors: List<AuthorEntity>,

    @Relation(
        parentColumn = "inAppId",
        entityColumn = "inAppBookId",
        entity = VoiceoverEntity::class
    )
    val voiceovers: List<DetailedVoiceover>,

    @Relation(
        parentColumn = "inAppId",
        entityColumn = "inAppBookId",
    )
    val externalIds: List<ExternalBookEntityId>
)

