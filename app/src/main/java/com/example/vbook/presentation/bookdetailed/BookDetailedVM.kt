package com.example.vbook.presentation.bookdetailed

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.bindService
import com.example.vbook.domain.usecases.GetFilledBook
import com.example.vbook.presentation.mediaservice.MediaService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.vbook.domain.common.Result
import com.example.vbook.isSuccess
import com.example.vbook.presentation.common.UiState
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class BookDetailedVM @Inject constructor(
    private val getFilledBook: GetFilledBook,
    @ApplicationContext context: Context
): ViewModel() {
    init {
        bindService(context){
            _serviceState.value=UiState.Success(it)
        }
    }
    private var _serviceState = MutableStateFlow<UiState<MediaService>>(UiState.Loading)
    var serviceState = _serviceState.asStateFlow()

    fun setServiceBook(bookUrl: String){
        viewModelScope.launch(Dispatchers.IO) {
            when(val book = getFilledBook(bookUrl)){
                is Result.Success-> {
                    withContext(Dispatchers.Main){
                        _serviceState.value.isSuccess {
                            it.data.setBook(book.data)
                        }
                    }
                }
                is Result.Error -> {
                    _serviceState.value.isSuccess {
                        it.data.onError(book.message)
                    }
                }
            }
        }
    }
}