package com.example.vbook.presentation.ui.bookdetailed

import android.app.DownloadManager
import android.content.*
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.bindService
import com.example.vbook.data.repository.book.BookRepository
import com.example.vbook.presentation.service.mediaservice.MediaService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.vbook.common.ResourceState
import com.example.vbook.common.model.Book
import com.example.vbook.data.repository.mediadownloadingitem.DownloadingItemRepository
import com.example.vbook.presentation.service.mediaservice.MediaPlayerManager
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
        val downloadsState = _downloadsState.value
        if (downloadsState is ResourceState.Success) {
            if (downloadingItem is ResourceState.Success) {
                val mutableDownloadsState = downloadsState.data.toMutableMap()
                mutableDownloadsState[downloadingItem.data.mediaOnlineUri] =
                    downloadingItemRepository.getDownloadStatus(downloadingItem.data.downloadId)
                setDownloadsStatus(mutableDownloadsState)
                val bookState = _bookState.value
                if (bookState is ResourceState.Success) {
                    val book = bookState.data
                    val itemToReplace =
                        book.mediaItems!!.first { downloadingItem.data.mediaOnlineUri == it.second }
                    val indexToReplace = book.mediaItems!!.indexOf(itemToReplace)
                    _service?.replaceMediaItem(
                        itemToReplace.second,
                        itemToReplace.first,
                        book.author.first,
                        book.coverURL,
                        indexToReplace
                    )
                }

            }
        }
    }

    fun onDownload(uri: String, book: Book) {
        viewModelScope.launch {
            downloadingItemRepository.createDownloadingItem(uri, book)
        }
    }

    fun init(bookUrl: String) {
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        context.registerReceiver(broadcastReceiver, intentFilter)
        viewModelScope.launch(Dispatchers.IO) {
            when (val book = bookRepository.getFilledBook(bookUrl)) {
                is ResourceState.Success -> {
                    withContext(Dispatchers.Main) {
                        setDownloadsStatus(getInitialDownloadsStatus(book.data))
                        bindBookToMediaService(book.data)
                    }
                }
                is ResourceState.Error -> _bookState.value = ResourceState.Error(book.message)
            }
        }
    }

    private suspend fun bindBookToMediaService(book: Book) {
        val (service, connection) = bindService(context)
        _service = service
        _serviceConnection = connection
        _service?.setBook(book)

        playbackMetadata = service.playbackInfo
        player = service.player

        _bookState.value = ResourceState.Success(book)
    }

    private fun setDownloadsStatus(statuses: Map<String, ResourceState<Unit>>) {
        _downloadsState.value = ResourceState.Success(statuses)
    }

    private suspend fun getInitialDownloadsStatus(book: Book): Map<String, ResourceState<Unit>> =
        downloadingItemRepository.getBookDownloadingItemsStatus(book)


    override fun onCleared() {
        super.onCleared()
        _serviceConnection?.let { context.unbindService(it) }
        _service = null
        _serviceConnection = null
        context.unregisterReceiver(broadcastReceiver)
    }
}