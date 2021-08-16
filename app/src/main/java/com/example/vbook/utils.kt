package com.example.vbook

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.vbook.data.db.model.BookEntity
import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

fun ThrowableResult(e:Throwable, tag:String="defError"): Result.Error {
    Log.e("UseCase $tag", e.stackTraceToString())
    return when(e){
        is IllegalStateException -> Result.Error(e.message+" Error")
        else -> Result.Error("Other Shit From ThrowableResult")
    }
}


fun Book.isDetailed():Boolean{
    return mp3List != null
}

fun BookEntity.isDetailed():Boolean{
    return mp3List != null
}

