/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.data.db.model.book

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "Reader",
    indices = [Index(value = ["fullName"], unique = true)]
)
data class ReaderEntity(

    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "fullName")
    val fullName: String,
)