package com.example.vbook.data.model



data class Book(
    var bookURL:String,
    var title:String,
    var author:Pair<String,String>,
    var reader:Pair<String,String>,
    var coverURL:String,
    var mp3List: List<Pair<String,String>>?=null,
    var cycle: List<Book>?=null,
    var duration: String?=null,
)
//Pair( title url)