package com.example.vbook

import android.util.Log
import com.example.vbook.data.db.model.BookEntity
import com.example.vbook.domain.common.Resource
import com.example.vbook.domain.model.Book
import java.lang.IllegalStateException

fun <T>ThrowableResource(e:Throwable, tag:String="defError"):Resource<T>{
    Log.e(tag, e.stackTraceToString())
    return when(e){
        is IllegalStateException -> Resource.Error(e.message+" Error")
        else -> Resource.Error("Other Shit")
    }
}


fun Book.isDetailed():Boolean{
    return mp3List != null
}

fun BookEntity.isDetailed():Boolean{
    return mp3List != null
}


