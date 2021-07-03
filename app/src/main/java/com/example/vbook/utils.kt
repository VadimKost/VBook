package com.example.vbook

import android.util.Log
import com.example.vbook.data.db.model.BookEntity
import com.example.vbook.domain.common.Resurce
import com.example.vbook.domain.model.Book
import java.lang.IllegalStateException

public fun <T>ThrowableHandler(e:Throwable):Resurce<T>{
    return when(e){
        is IllegalStateException -> Resurce.Error(e.message+" Error")
        else -> {
            Log.e("VVV", e.stackTraceToString())
            Resurce.Error("Other Shit")
        }
    }
}
