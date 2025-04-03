/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.data.db.model.book

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MediaItem")
data class MediaItemEntity(

    @PrimaryKey
    val url: String,

    @ColumnInfo(name = "voiceoverId")
    val voiceoverId: String? = null,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "duration")
    val duration: Long
)
