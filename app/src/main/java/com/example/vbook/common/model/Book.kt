package com.example.vbook.common.model

data class Book(
    var source:String,
    var bookURL:String,
    var title:String,
    var author:Pair<String,String>,
    var reader:Pair<String,String>,
    var coverURL:String,
    var mediaItems: List<Pair<String,String>>?=null,
    val cycle:Pair<String,String>,
    var cycleBookList: List<Pair<String,String>>?=null,
    var duration: String?=null,

    var isFavorite:Boolean=false,

    var stoppedTrackIndex:Int=0,
    var stoppedTrackTime: Long=0,
)

//Pair( title url)