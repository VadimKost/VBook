package com.example.vbook.data.model



data class Book(
    var bookURL:String,
    var title:String,
    var author:Pair<String,String>,
    var reader:Pair<String,String>,
    var coverURL:String,
    var mp3List: List<Pair<String,String>>?=null,
    val cycle:Pair<String,String>,
    var cycleBookList: List<Pair<String,String>>?=null,
    var duration: String?=null,
)
//Pair( title url)