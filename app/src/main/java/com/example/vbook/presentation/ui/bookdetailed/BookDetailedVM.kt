package com.example.vbook.presentation.ui.bookdetailed

import android.app.DownloadManager
import android.content.*
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.bindService
import com.example.vbook.data.repository.book.BookRepository
import com.example.vbook.service.mediaservice.MediaService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.vbook.common.ResourceState
import com.example.vbook.common.model.Book
import com.example.vbook.data.repository.mediadownloadingitem.DownloadingItemRepository
import com.example.vbook.downloadmanager.MediaDownloadManager
import com.example.vbook.service.mediaservice.MediaPlayerManager
import com.google.android.exoplayer2.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class BookDetailedVM @Inject constructor(
    val bookRepository: BookRepository,
    val downloadingItemRepository: DownloadingItemRepository,
    val mediaDownloadManager: MediaDownloadManager,
    @ApplicationContext val context: Context
) : ViewModel() {

    private var _service: MediaService? = null
    private var _serviceConnection: ServiceConnection? = null

    private val _bookState: MutableStateFlow<ResourceState<Book>> =
        MutableStateFlow(ResourceState.Loading)
    val bookState = _bookState.asStateFlow()

    private val _downloadsState: MutableStateFlow<ResourceState<Map<String, ResourceState<Unit>>>> =
        MutableStateFlow(ResourceState.Loading)
    val downloadsState = _downloadsState.asStateFlow()

    lateinit var playbackMetadata: Flow<MediaPlayerManager.PlaybackInfo>
    lateinit var player: ExoPlayer

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.e("Offline","update")
            val deferred = goAsync()
            val downloadId = intent.getLongExtra(
                DownloadManager.EXTRA_DOWNLOAD_ID, -1L
            )
            viewModelScope.launch {
                onDownloadComplete(downloadId)
                deferred.finish()
            }

        }

    }

    private suspend fun onDownloadComplete(downloadId: Long) {
        val downloadingItem = downloadingItemRepository.getMediaItemDownloadByDownloadId(downloadId)
        Log.e("Sosi", "$downloadingItem")
        val downloadsState = _downloadsState.value
        Log.e("Sosi","almost update2 ${downloadId}")
        if (downloadsState is ResourceState.Success) {
            Log.e("Sosi","almost updated1")
            if (downloadingItem is ResourceState.Success) {
                Log.e("Sosi","almost updated")
                val mutableDownloadsState = downloadsState.data.toMutableMap()
                mutableDownloadsState[downloadingItem.data.bookUrl] =
                    mediaDownloadManager.getDownloadState(downloadingItem.data.downloadId)
                setDownloadsStatus(mutableDownloadsState)
            }
        }
    }

    fun onDownload(uri: String,book: Book){
        viewModelScope.launch {
            mediaDownloadManager.initiateDownload(uri, book)
        }
    }

    fun init(bookUrl: String) {
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        context.registerReceiver(broadcastReceiver,intentFilter)
        viewModelScope.launch(Dispatchers.IO){
            when (val book = bookRepository.getFilledBook(bookUrl)) {
                is ResourceState.Success -> {
                    withContext(Dispatchers.Main) {
                        bindBookToMediaService(book.data)
                        setDownloadsStatus(getInitialDownloadsStatus(book.data))
                    }
                }
                is ResourceState.Error -> _bookState.value = ResourceState.Error(book.message)
            }
        }
    }

    private suspend fun bindBookToMediaService(book: Book) {
        val (service, connection) = bindService(context)
        _service?.setBook(book)

        _service = service
        _serviceConnection = connection

        playbackMetadata = service.playbackInfo
        player = service.player

        _bookState.value = ResourceState.Success(book)
    }

    private fun setDownloadsStatus(statuses: Map<String, ResourceState<Unit>>) {
        _downloadsState.value = ResourceState.Success(statuses)
    }

    private suspend fun getInitialDownloadsStatus(book: Book): Map<String, ResourceState<Unit>> {
        val mediaUrls = book.mp3List!!.map { it.second }
        val status = mutableMapOf<String, ResourceState<Unit>>()
        val downloadingItems =
            downloadingItemRepository.getMediaItemDownloadsByBookUrl(book.bookURL)
        if (downloadingItems is ResourceState.Empty) {
            mediaUrls.forEach { mediaUrl ->
                status[mediaUrl] = ResourceState.Empty
            }
            return status
        }
        if (downloadingItems is ResourceState.Success) {
            mediaUrls.forEach { mediaUrl ->
                if (mediaDownloadManager.hasLocalCopy()) {
                    status[mediaUrl] = ResourceState.Success(Unit)
                } else {
                    val downloadingItem =
                        downloadingItems.data.filter { it.mediaOnlineUri == mediaUrl }
                    if (downloadingItem.isEmpty()) status[mediaUrl] = ResourceState.Empty else{
                        val downloadingState =
                            mediaDownloadManager.getDownloadState(downloadingItem.first().downloadId)
                        status[mediaUrl] = downloadingState
                    }

                }
            }
            return status
        }
        return status
    }

    override fun onCleared() {
        super.onCleared()
        _serviceConnection?.let { context.unbindService(it) }
        _service = null
        _serviceConnection = null
        context.unregisterReceiver(broadcastReceiver)
    }
}