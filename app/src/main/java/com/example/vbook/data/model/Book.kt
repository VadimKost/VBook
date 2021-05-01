package com.example.vbook.data.model



data class Book(
    var title:String,
    var author:Pair<String,String>,
    var reader:Pair<String,String>,
    var coverURL:String,
    var mp3List: List<Pair<String,String>>?,
    var cycle: List<Pair<String,String>>?,
    var duration: Float?,

)
