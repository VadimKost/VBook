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
    var mediaOnlineUri:String,
    var downloadId:Long,
    var bookUrl:String
)
