package com.example.vbook.device

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.vbook.device.service.mediaservice.MediaService
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun bindService(context: Context): Pair<MediaService, ServiceConnection> {
    return suspendCoroutine { continuation ->
        context.bindService(Intent(context, MediaService::class.java), object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                Log.d("Service","Here3")
                val playerServiceBinder = service as MediaService.PlayerServiceBinder
                val service = playerServiceBinder.service.get()!!
                continuation.resume(Pair(service, this))
            }

            override fun onServiceDisconnected(name: ComponentName?) {
            }
        }, Context.BIND_AUTO_CREATE)
    }
}