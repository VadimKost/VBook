package com.example.vbook

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.Composable
import com.example.vbook.data.db.model.BookEntity
import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.Book
import com.example.vbook.presentation.common.UiState
import com.example.vbook.presentation.mediaservice.MediaService
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

fun <T> getStateData(state:UiState<T>): T? {
    if (state is UiState.Success){
        return state.data
    }else{
        return null
    }
}
@Composable
fun <T> UiState<T>.IsSuccess(action: @Composable (UiState.Success<T>) -> Unit){
    if (this is UiState.Success<T>){
        action(this)
    }
}

fun <T> UiState<T>.isSuccess(action:(UiState.Success<T>) -> Unit){
    if (this is UiState.Success<T>){
        action(this)
    }
}

fun bindService(context:Context,action:(MediaService)->Unit){
    context.bindService(Intent(context, MediaService::class.java),object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val playerServiceBinder = service as MediaService.PlayerServiceBinder
            val service = playerServiceBinder.service
            action(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }, Context.BIND_AUTO_CREATE)
}

