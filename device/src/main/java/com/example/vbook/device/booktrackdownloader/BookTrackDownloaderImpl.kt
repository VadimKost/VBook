//package com.example.vbook.device.booktrackdownloader
//
//import android.app.DownloadManager
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.net.Uri
//import androidx.core.content.ContextCompat
//import com.example.vbook.domain.ResourceState
//import com.example.vbook.domain.service.BookTrackDownloader
//import com.example.vbook.domain.model.DownloadingItem
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class BookTrackDownloaderImpl @Inject constructor(
//    val downloadManager: DownloadManager,
//    @ApplicationContext val context: Context
//) : BookTrackDownloader {
//    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
//
//    private val receiverState = MutableStateFlow<ResourceState<Long>>(ResourceState.Loading)
//    var bound = MutableStateFlow(false)
//    // TODO: Add download queue
//
//    override fun startDownloading(uri: String, downloadingTitle: String): Long {
//        val downloadRequest = createDownloadRequest(Uri.parse(uri), downloadingTitle)
//        val downloadId = downloadManager.enqueue(downloadRequest)
//        return downloadId
//    }
//
//    private fun createDownloadRequest(uri: Uri, title: String) =
//        DownloadManager.Request(uri)
//            .setTitle(title)
//            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//            .setDestinationInExternalFilesDir(context, "", uri.path)
//            .setVisibleInDownloadsUi(false)
//
//    override fun getDownloadStatus(downloadId: Long): DownloadingItem.Status {
//        val request = DownloadManager.Query().setFilterById(downloadId)
//        val downloadingStatus = downloadManager.query(request).use {
//            if (it.count > 0) {
//                it.moveToFirst()
//                val statusIndex = it.getColumnIndex(DownloadManager.COLUMN_STATUS)
//                it.getInt(statusIndex)
//            } else {
//                null
//            }
//        }
//        return when (downloadingStatus) {
//            null ->
//                DownloadingItem.Status.EMPTY
//
//            DownloadManager.STATUS_FAILED ->
//                DownloadingItem.Status.FAILED
//
//            DownloadManager.STATUS_RUNNING -> DownloadingItem.Status.DOWNLOADING
//
//            DownloadManager.STATUS_PENDING,
//            DownloadManager.STATUS_PAUSED ->
//                DownloadingItem.Status.STOPPED
//
//            DownloadManager.STATUS_SUCCESSFUL ->
//                DownloadingItem.Status.SUCCESS
//
//            else -> {
//                DownloadingItem.Status.EMPTY
//            }
//        }
//    }
//
//    override fun getLocalUriByDownloadId(downloadId: Long): ResourceState<String> {
//        val localUri = downloadManager.getUriForDownloadedFile(downloadId)?.toString()
//        if (localUri != null)  return ResourceState.Success(localUri)
//        return ResourceState.Empty
//    }
//
//    override fun receiveDownloadedEvent(): StateFlow<ResourceState<Long>> = receiverState
//
//
//    private val broadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val deferred = goAsync()
//            val downloadId = intent.getLongExtra(
//                DownloadManager.EXTRA_DOWNLOAD_ID, -1L
//            )
//            scope.launch {
//                receiverState.value = ResourceState.Success(downloadId)
//                deferred.finish()
//            }
//        }
//
//
//    }
//
//    init {
//        scope.launch {
//            receiverState.subscriptionCount.collect {
//                if (it > 0 && !bound.value) {
//                    onBind()
//                    bound.value = true
//                    receiverState.value = ResourceState.Empty
//                } else if (it == 0) {
//                    receiverState.value = ResourceState.Loading
//                    bound.value = false
//                    onUnbind()
//                }
//            }
//        }
//    }
//
//    private fun onBind() {
//        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
//        ContextCompat.registerReceiver(context,broadcastReceiver, intentFilter, ContextCompat.RECEIVER_NOT_EXPORTED)
//    }
//
//    private fun onUnbind() {
//        try {
//            context.unregisterReceiver(broadcastReceiver)
//        }catch(e:IllegalArgumentException){
//            println("Caught")
//        }
//
//    }
//}