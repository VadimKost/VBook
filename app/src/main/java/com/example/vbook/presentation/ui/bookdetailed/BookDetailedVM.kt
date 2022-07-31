package com.example.vbook.presentation.ui.bookdetailed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.vbook.domain.ResourceState
import com.example.vbook.domain.bookplayer.IsConnecting
import com.example.vbook.domain.bookplayer.TransferringData
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.model.DownloadingItem
import com.example.vbook.domain.model.player.PlayerMetadata
import com.example.vbook.domain.usecase.book.GetFilledBookByUrlUseCase
import com.example.vbook.domain.usecase.book.SetBookIsFavoriteUseCase
import com.example.vbook.domain.usecase.booktrakdownloader.ReceiveDownloadedEventUseCase
import com.example.vbook.domain.usecase.combination.GetBookDownloadingItemsStatusUseCase
import com.example.vbook.domain.usecase.combination.OnDownloadCompleteUseCase
import com.example.vbook.domain.usecase.combination.StartDownloadAndCreateDownloadingItemUseCase
import com.example.vbook.domain.usecase.mediaplayer.GetPlayerStateUseCase
import com.example.vbook.domain.usecase.mediaplayer.PlayerUseCases
import com.example.vbook.domain.usecase.mediaplayer.playback_controll.PlayUseCase
import com.example.vbook.domain.usecase.mediaplayer.playback_controll.SetPlayerBookPlaylistUseCase
import kotlinx.coroutines.flow.*
import javax.inject.Inject

//data class BookDetailedScreenUi(
//    val book: Book, val playbackInfo: PlaybackInfo, val isServiceBookSame: Boolean
//)

@HiltViewModel
class BookDetailedVM @Inject constructor(
    val getFilledBookByUrlUseCase: GetFilledBookByUrlUseCase,
    val setBookIsFavoriteUseCase: SetBookIsFavoriteUseCase,
    val startDownloadAndCreateDownloadingItemUseCase: StartDownloadAndCreateDownloadingItemUseCase,
    val getBookDownloadingItemsStatusUseCase: GetBookDownloadingItemsStatusUseCase,
    val receiveDownloadedEventUseCase: ReceiveDownloadedEventUseCase,
    val onDownloadCompleteUseCase: OnDownloadCompleteUseCase,
    val playerUseCases: PlayerUseCases
) : ViewModel() {

    private val _localBook: MutableStateFlow<ResourceState<Book>> =
        MutableStateFlow(ResourceState.Loading)
    val localBook = _localBook.asStateFlow()

    private val _downloadsState: MutableStateFlow<ResourceState<Map<String, DownloadingItem.Status>>> =
        MutableStateFlow(ResourceState.Loading)
    val downloadsState = _downloadsState.asStateFlow()

    private val playerState = playerUseCases.getPlayerStateUseCase()

    val playerBook = playerState
        .filterIsInstance<TransferringData<PlayerMetadata>>()
        .map { TransferringData(it.data.book) }
        .stateIn(viewModelScope, SharingStarted.Lazily, IsConnecting)

    val playbackInfo = playerState
        .filterIsInstance<TransferringData<PlayerMetadata>>()
        .map { TransferringData(it.data.playbackInfo) }
        .stateIn(viewModelScope, SharingStarted.Lazily, IsConnecting)

    val isPlayerBookIsSame = combine(playerBook, localBook) { playerBookState, localBookState ->
        playerBookState.onSuccess { playerBook ->
            localBookState.onSuccess { localBook ->
                return@combine playerBook == localBook
            }
        }
        return@combine false
    }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)


    fun init(bookUrl: String) {
        viewModelScope.launch {
            when (val filledBook = getFilledBookByUrlUseCase(bookUrl)) {
                is ResourceState.Success -> {
                    setDownloadsStatus(getBookDownloadingItemsStatusUseCase(filledBook.data))
                    _localBook.value = filledBook
                }
                is ResourceState.Error -> _localBook.value = ResourceState.Error(filledBook.message)
                else -> {}
            }
        }
        registerDownloadedEventReceiver()
    }

    private fun registerDownloadedEventReceiver() {
        viewModelScope.launch {
            receiveDownloadedEventUseCase()
                .filterIsInstance<TransferringData<Long>>()
                .collect {
                    val downloadId = it.data
                    val book = localBook.value.getOrNull()
                    if (book != null) {
                        val url = onDownloadCompleteUseCase(downloadId, book)
                        url.onSuccess { uri ->
                            downloadsState.value.onSuccess {
                                //TODO UPDATE STATE
                            }
                        }
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
                        startDownloadAndCreateDownloadingItemUseCase(uri, book)
                        val newStatuses = downloadingStatus.data.toMutableMap()
                        newStatuses[uri] = DownloadingItem.Status.DOWNLOADING
                        _downloadsState.value = ResourceState.Success(newStatuses)
                    }
                }
                DownloadingItem.Status.DOWNLOADING -> {}
                DownloadingItem.Status.SUCCESS -> {}
                else -> {}
            }
        }

    }

    private fun setDownloadsStatus(statuses: Map<String, DownloadingItem.Status>) {
        _downloadsState.value = ResourceState.Success(statuses)
    }

    fun setIsBookFavorite(isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _localBook.value.onSuccess {
                _localBook.value = ResourceState.Success(it.copy(isFavorite = isFavorite))
                setBookIsFavoriteUseCase(it.bookUrl, isFavorite)
            }
        }
    }

    fun play() {
        viewModelScope.launch {
            localBook.value.onSuccess {
                playerUseCases.setPlayerBookPlaylistUseCase(it)
                playerUseCases.getPlayerStateUseCase()
                    .filterIsInstance<TransferringData<PlayerMetadata>>().first()
                playerUseCases.playUseCase()
            }
        }
    }

    fun pause() {
        playerUseCases.pauseUseCase()
    }

    fun seekBack() {
        playerUseCases.rewindUseCase()
    }

    fun seekForward() {
        playerUseCases.fastForwardUseCase()
    }

    fun seekToNextWindow() {
        playerUseCases.nextUseCase()
    }

    fun seekToPreviousWindow() {
        playerUseCases.previousUseCase()
    }

    fun seekTo(value: Long) {
        playbackInfo.value.onSuccess {
            playerUseCases.seekToUseCae(it.trackIndex, value)
        }
    }
}