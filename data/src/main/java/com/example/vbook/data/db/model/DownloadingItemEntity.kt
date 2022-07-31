/*
 * Created by Vadim on 13.07.22, 12:17
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 09.07.22, 20:35
 *
 */

package com.example.vbook.data.db.model

import androidx.room.*
import com.example.vbook.data.db.TypeConvertor

@Entity(tableName = "MediaItemDownloadEntity",
    foreignKeys = [ForeignKey(
        entity = BookEntity::class,
        parentColumns = arrayOf("bookURL"),
        childColumns = arrayOf("bookUrl"),
        onDelete = ForeignKey.CASCADE
    )])
@TypeConverters(TypeConvertor::class)
data class DownloadingItemEntity(
    @PrimaryKey
    var downloadId:Long,
    val onlineUri:String,
    var bookUrl:String
)
