package com.example.vbook

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.Composable
import com.example.vbook.data.db.model.BookEntity
import com.example.vbook.presentation.model.Book
import com.example.vbook.common.ResourceState
import com.example.vbook.presentation.service.mediaservice.MediaService
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun Book.isDetailed(): Boolean {
    return mp3List != null
}

fun BookEntity.isDetailed(): Boolean {
    return mp3List != null
}

@Composable
fun <T> ResourceState<T>.IsSuccess(action: @Composable (ResourceState.Success<T>) -> Unit) {
    if (this is ResourceState.Success<T>) {
        action(this)
    }
}

fun <T> ResourceState<T>.isSuccess(action: (ResourceState.Success<T>) -> Unit) {
    if (this is ResourceState.Success<T>) {
        action(this)
    }
}

fun Long.toTime(): String {
    val minutes = this / 1000 / 60
    val seconds = this / 1000 % 60
    if (seconds < 10) {
        return "$minutes:0$seconds"
    }
    return "$minutes:$seconds"
}

fun Long.toSliderFloat(duration: Long): Float {
    if (this != 0L && duration != 0L) {
        return ((this * 100).toFloat()) / duration.toFloat()
    } else return 0f

}


suspend fun bindService(context: Context): Pair<MediaService, ServiceConnection> {
    return suspendCoroutine { continuation ->
        context.bindService(Intent(context, MediaService::class.java), object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val playerServiceBinder = service as MediaService.PlayerServiceBinder
                val service = playerServiceBinder.service
                continuation.resume(Pair(service, this))
            }

            override fun onServiceDisconnected(name: ComponentName?) {
            }
        }, Context.BIND_AUTO_CREATE)
    }
}

