/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.presentation.ui.screen.bookdetailed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.domain.application.usecase.ObserveBookByInAppIdUseCase
import com.example.vbook.domain.application.usecase.ObservePlayerStateUseCase
import com.example.vbook.domain.book.model.Book
import com.example.vbook.domain.player.port.MediaPlayer
import com.example.vbook.domain.shared.ResourceState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


@HiltViewModel(assistedFactory = BookDetailedVM.Factory::class)
class BookDetailedVM @AssistedInject constructor(
    observeBookByInAppIdUseCase: ObserveBookByInAppIdUseCase,
    observePlayerStateUseCase: ObservePlayerStateUseCase,
    @Assisted val inAppBookId: String
) : ViewModel() {

    fun onPlaybackControlEvent(command: MediaPlayer.PlaybackControlCommand) {

    }

    private val book: Flow<ResourceState<Book>> = observeBookByInAppIdUseCase(inAppBookId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ResourceState.Loading
        )

    private val playerState: Flow<MediaPlayer.State> = observePlayerStateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MediaPlayer.State.Initialization
        )

//    private val _downloads: Flow<ResourceState<Map<String, DownloadingItem.Status>>> =
//        MutableStateFlow(ResourceState.Loading)
//
//    private val _isShowDownloadDialog = MutableStateFlow(false)
//
//    private val _playerState = getPlayerStateUseCase()

    val state = combine(
        book,
        playerState,
    ) { book, playerState ->
        BookDetailedUIState(
            book = book,
            playerState = playerState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BookDetailedUIState()
    )

//    init {
//        viewModelScope.launch {
//            registerDownloadedEventReceiver()
//        }
//    }


//    private fun registerDownloadedEventReceiver() {
//        viewModelScope.launch {
//            receiveDownloadedEventUseCase()
//                .filterIsInstance<TransferringData<Long>>()
//                .collect {
//                    val downloadId = it.data
//                    _book.filterIsInstance<ResourceState.Success<Book>>().collect { book ->
//                        val url = onDownloadCompleteUseCase(downloadId, book.data)
//                        url.onSuccess { uri ->
//                            _downloads.value = emptyMap()
//                            //TODO UPDATE STATE
//
//                        }
//
//                    }
//                }
//        }
//    }

//    fun onDownloadClick(uri: String, book: Book) {
//        val downloadingStatus = state.value.downloads
//        if (downloadingStatus.isSuccess()) {
//            when (downloadingStatus.data[uri]) {
//                DownloadingItem.Status.EMPTY -> {
//                    viewModelScope.launch {
//                        startDownloadAndCreateDownloadingItemUseCase(uri, book)
//                        val newStatuses = downloadingStatus.data.toMutableMap()
//                        newStatuses[uri] = DownloadingItem.Status.DOWNLOADING
////                    _downloads.value = newStatuses
//                    }
//                }
//
//                DownloadingItem.Status.DOWNLOADING -> {}
//                DownloadingItem.Status.SUCCESS -> {}
//                else -> {}
//            }
//        }
//
//
//    }

//    fun onDownloadDialogToggle(show: Boolean) {
//        _isShowDownloadDialog.update { show }
//    }
//
//    fun onBookFavorite(isFavorite: Boolean) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val book = state.value.book
//            if (book.isSuccess()) {
//                setBookIsFavoriteUseCase(book.data.bookUrl, isFavorite)
//            }
//        }
//    }

//    fun onPlaybackControlEvent(event: PlaybackControlUseCase.PlaybackControlCommand) {
//        viewModelScope.launch {
//            val bookState = state.value.book
//            if (event is PlaybackControlUseCase.PlaybackControlCommand.Play && bookState.isSuccess()) {
//                setPlayerBookPlaylistUseCase(bookState.data)
//            }
//            playbackControlUseCase(event)
//        }
//
//
//    }

    @AssistedFactory
    interface Factory {
        fun create(inAppBookId: String): BookDetailedVM
    }
}