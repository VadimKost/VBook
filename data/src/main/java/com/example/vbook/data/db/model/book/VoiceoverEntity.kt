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
import com.example.vbook.data.db.model.book.crossref.VoiceoverReaderCrossRef
import java.util.UUID

@Entity(tableName = "Voiceover")
data class VoiceoverEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "inAppBookId")
    val inAppBookId: String? = null,
)

data class DetailedVoiceover(

    @Embedded
    val voiceoverEntity: VoiceoverEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = VoiceoverReaderCrossRef::class,
            parentColumn = "voiceoverId",
            entityColumn = "readerId"
        )
    )
    val readers: List<ReaderEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "voiceoverId",
    )
    val mediaItems: List<MediaItemEntity>,
)
