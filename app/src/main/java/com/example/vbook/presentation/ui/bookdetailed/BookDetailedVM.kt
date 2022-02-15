package com.example.vbook.presentation.ui.bookdetailed

import android.content.Context
import android.content.ServiceConnection
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
    @ApplicationContext val context: Context
) : ViewModel() {
    private var _service: MediaService? = null
    private var _serviceConnection: ServiceConnection? = null

    private val _bookState: MutableStateFlow<ResourceState<Book>> = MutableStateFlow(ResourceState.Loading)
    val bookState = _bookState.asStateFlow()

    lateinit var playbackMetadata: Flow<MediaPlayerManager.PlaybackInfo>
    lateinit var player:ExoPlayer

    fun bindToMediaService(bookUrl: String){
        viewModelScope.launch {
            val (service, connection) = bindService(context)
            _service = service
            _serviceConnection = connection

            playbackMetadata = service.playbackInfo
            player = service.player
            withContext(Dispatchers.IO){setServiceBook(bookUrl)}



        }
    }

    private suspend fun setServiceBook(bookUrl: String){
        when(val book = bookRepository.getFilledBook(bookUrl)){
            is ResourceState.Success -> {
                withContext(Dispatchers.Main) {
                    _service?.setBook(book.data)
                    _bookState.value = ResourceState.Success(book.data)
                }
            }
            is ResourceState.Error -> _bookState.value = ResourceState.Error(book.message)
        }
    }

    override fun onCleared() {
        super.onCleared()
        _serviceConnection?.let { context.unbindService(it) }
        _service = null
        _serviceConnection = null
    }
}