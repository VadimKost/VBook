package com.example.vbook.domain.model

import androidx.room.ColumnInfo

data class Book(
    var source:String,
    var bookURL:String,
    var title:String,
    var author:Pair<String,String>,
    var reader:Pair<String,String>,
    var coverURL:String,
    var mp3List: List<Pair<String,String>>?=null,
    val cycle:Pair<String,String>,
    var cycleBookList: List<Pair<String,String>>?=null,
    var duration: String?=null,
    var isCurrent:Boolean,
    var stoppedTrackIndex:Int=0,
    var stoppedTrackTime: Float=0.0F,
)
//Pair( title url)