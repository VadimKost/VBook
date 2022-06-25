package com.example.vbook.presentation.ui.bookdetailed

import android.app.DownloadManager
import android.content.*
import android.util.Log
import android.widget.Toast
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
import com.example.vbook.common.model.DownloadingItem
import com.example.vbook.data.repository.mediaitem.DownloadingItemRepository
import com.example.vbook.presentation.service.mediaservice.MediaPlayerManager
import com.google.android.exoplayer2.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class BookDetailedVM @Inject constructor(
    val bookRepository: BookRepository,
    val downloadingItemRepository: DownloadingItemRepository,
    @ApplicationContext val context: Context
) : ViewModel() {
    private val _serviceState: MutableStateFlow<ResourceState<MediaService>> =
        MutableStateFlow(ResourceState.Loading)

    private var _serviceConnection: ServiceConnection? = null

    private val _bookState: MutableStateFlow<ResourceState<Book>> =
        MutableStateFlow(ResourceState.Loading)
    val bookState = _bookState.asStateFlow()

    val isServiceBookSame = flow {
        _serviceState.collect{
            val serviceBookState = it.getOrNull()?.serviceBook
            if (serviceBookState != null){
                combine(serviceBookState,bookState){serviceBook,currentBook ->
                    return@combine serviceBook == currentBook.getOrNull()
                }.collect{
                    emit(it)
                }

            }
        }
    }

    private val _downloadsState: MutableStateFlow<ResourceState<Map<String, DownloadingItem.Status>>> =
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
                    _serviceState.value.getOrNull()?.replaceMediaItem(
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

    fun onDownloadClick(uri: String, book: Book) {
        val downloadingStatus = downloadsState.value
        if (downloadingStatus is ResourceState.Success) {
            when (downloadingStatus.data[uri]) {
                DownloadingItem.Status.EMPTY -> {
                    viewModelScope.launch {
                        downloadingItemRepository.createDownloadingItem(uri, book)
                        val newStatuses =
                            downloadingStatus.data.toMutableMap()
                        newStatuses[uri] = DownloadingItem.Status.DOWNLOADING
                        _downloadsState.value = ResourceState.Success(newStatuses)
                    }
                }
                DownloadingItem.Status.DOWNLOADING -> {
                    Toast.makeText(context, "Fake Cancel", Toast.LENGTH_SHORT).show()
                }
                DownloadingItem.Status.SUCCESS -> {
                    Toast.makeText(context, "Fake Delete", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

    }

    fun init(bookUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val book = bookRepository.getFilledBook(bookUrl)) {
                is ResourceState.Success -> {
                    withContext(Dispatchers.Main) {
                        setDownloadsStatus(getInitialDownloadsStatus(book.data))
                        registerBroadcastReceiver()
                        bindBookToMediaService(book.data)
                    }
                }
                is ResourceState.Error -> _bookState.value = ResourceState.Error(book.message)
                else -> {}
            }
        }
    }

    fun registerBroadcastReceiver() {
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        context.registerReceiver(broadcastReceiver, intentFilter)
    }

    private suspend fun bindBookToMediaService(book: Book) {
        val (service, connection) = bindService(context)
        _serviceState.value = ResourceState.Success(service)
        _serviceConnection = connection

        playbackMetadata = service.playbackInfo
        player = service.player

        _bookState.value = ResourceState.Success(book)
    }

    private fun setDownloadsStatus(statuses: Map<String, DownloadingItem.Status>) {
        _downloadsState.value = ResourceState.Success(statuses)
    }

    private suspend fun getInitialDownloadsStatus(book: Book): Map<String, DownloadingItem.Status> =
        downloadingItemRepository.getBookDownloadingItemsStatus(book)

    fun setIsBookFavorite(isFavorite: Boolean) {
        Log.e("Book", "Book update to $isFavorite")
        viewModelScope.launch(Dispatchers.IO) {
            val bookState = _bookState.value
            if (bookState is ResourceState.Success) {
                bookRepository.setIsBookFavorite(bookState.data.bookURL, isFavorite)
                _bookState.value =
                    ResourceState.Success(bookState.data.copy(isFavorite = isFavorite))
            }
        }
    }

    fun onPlay() {
        bookState.value.onSuccess { bookState ->
            viewModelScope.launch {
                _serviceState.value.getOrNull()?.setBook(bookState)
                player.play()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _serviceConnection?.let { context.unbindService(it) }
        _serviceConnection = null
        context.unregisterReceiver(broadcastReceiver)
    }
}