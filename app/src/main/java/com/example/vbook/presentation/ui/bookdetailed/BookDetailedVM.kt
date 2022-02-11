package com.example.vbook.presentation.ui.bookdetailed

import android.app.DownloadManager
import android.content.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.bindService
import com.example.vbook.domain.usecases.GetFilledBookUseCase
import com.example.vbook.presentation.service.mediaservice.MediaService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.usecases.GetMediaItemDownloadUseCase
import com.example.vbook.presentation.common.UiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class BookDetailedVM @Inject constructor(
    private val getFilledBook: GetFilledBookUseCase,
    getMediaItemDownloadUseCase: GetMediaItemDownloadUseCase,
    @ApplicationContext val context: Context
) : ViewModel() {
    private var _serviceState = MutableStateFlow<UiState<MediaService>>(UiState.Loading)
    var serviceState = _serviceState.asStateFlow()

    private var _mediaItemDownloadStatusList = MutableStateFlow<List<UiState<Unit>>>(listOf())
    var mediaItemDownloadStatusList = _mediaItemDownloadStatusList.asStateFlow()

    private var serviceConnection: ServiceConnection? = null

    private val downloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val deferred = goAsync()
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            viewModelScope.launch {
                val download = getMediaItemDownloadUseCase(downloadId)
//                val status = getDownloadStatus(downloadId)
                deferred.finish()
            }

        }
    }

    init {
        viewModelScope.launch {
            val (service, connection) = bindService(context)
            _serviceState.value = UiState.Success(service)
            serviceConnection = connection
            val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
            context.registerReceiver(downloadReceiver, intentFilter)
        }
    }

    fun setServiceBook(bookUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val book = getFilledBook(bookUrl)) {
                is Result.Success -> {
                    withContext(Dispatchers.Main) {
                        val state = _serviceState.value
                        if (state is UiState.Success) {
                            state.data.setBook(book.data)
                            _mediaItemDownloadStatusList.value = getDownloadsState(book.data)
                        }

                    }
                }
                is Result.Error -> {
                    _serviceState.value = UiState.Error(book.message)
                }
            }
        }
    }

    suspend fun getDownloadsState(book: Book): List<UiState<Unit>> {
        return List(book.mp3List!!.size) { UiState.Success(Unit) }
    }

    override fun onCleared() {
        super.onCleared()
        serviceConnection?.also {
            context.unbindService(it)
        }
    }
}