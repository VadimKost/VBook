package com.example.vbook.presentation.ui.bookdetailed

import android.content.Context
import android.content.ServiceConnection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.bindService
import com.example.vbook.domain.usecases.GetFilledBookUseCase
import com.example.vbook.presentation.service.mediaservice.MediaService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.vbook.domain.common.Result
import com.example.vbook.isSuccess
import com.example.vbook.presentation.common.UiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class BookDetailedVM @Inject constructor(
    private val getFilledBook: GetFilledBookUseCase,
    @ApplicationContext val context: Context
) : ViewModel() {
    private var _serviceState = MutableStateFlow<UiState<MediaService>>(UiState.Loading)
    var serviceState = _serviceState.asStateFlow()

    private var serviceConnection: ServiceConnection? = null

    init {
        viewModelScope.launch {
            val (service, connection) = bindService(context)
            _serviceState.value = UiState.Success(service)
            serviceConnection = connection
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
                        }

                    }
                }
                is Result.Error -> {
                    _serviceState.value = UiState.Error(book.message)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        serviceConnection?.also {
            context.unbindService(it)
        }
    }
}