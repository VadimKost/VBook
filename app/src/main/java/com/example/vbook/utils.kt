package com.example.vbook

import android.util.Log
import com.example.vbook.domain.common.Resurce
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