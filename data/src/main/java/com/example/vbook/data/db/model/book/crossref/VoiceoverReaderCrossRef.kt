/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.data.db.model.book.crossref

import androidx.room.Entity


@Entity(primaryKeys = ["readerId", "voiceoverId"])
data class VoiceoverReaderCrossRef(
    val readerId: String,
    val voiceoverId: String
)