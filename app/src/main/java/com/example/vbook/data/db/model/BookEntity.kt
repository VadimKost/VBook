package com.example.vbook.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.vbook.data.db.TypeConvertor

@Entity(tableName = "BookEntity")
@TypeConverters(TypeConvertor::class)
data class BookEntity(
    @ColumnInfo(name = "source") var source:String,
    @PrimaryKey
    @ColumnInfo(name = "bookURL") var bookURL:String,
    @ColumnInfo(name = "title") var title:String,
    @ColumnInfo(name = "author") var author:Pair<String,String>,
    @ColumnInfo(name = "reader") var reader:Pair<String,String>,
    @ColumnInfo(name = "coverURL") var coverURL:String,
    @ColumnInfo(name = "mp3List") var mediaItems: List<Pair<String,String>>?=null,
    @ColumnInfo(name = "cycle") val cycle:Pair<String,String>,
    @ColumnInfo(name = "cycleBookList") var cycleBookList: List<Pair<String,String>>?=null,
    @ColumnInfo(name = "duration") var duration: String?=null,
    @ColumnInfo(name = "isCurrent") var isCurrent:Boolean,
    @ColumnInfo(name = "stoppedTrackIndex")var stoppedTrackIndex:Int,
    @ColumnInfo(name = "stoppedTrackTime")var stoppedTrackTime:Long,

    )

