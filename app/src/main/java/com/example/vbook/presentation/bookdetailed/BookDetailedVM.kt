package com.example.vbook.presentation.bookdetailed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.domain.common.Result
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.usecases.GetFilledBook
import com.example.vbook.presentation.mediaservice.MediaService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BookDetailedVM @Inject constructor(
    private val getFilledBook: GetFilledBook
): ViewModel() {


    fun setServiceBook(service:MediaService,bookUrl:String): Deferred<Book?> {
        return viewModelScope.async(IO) {
            val book = when(val bookResource = getFilledBook(bookUrl)){
                is Result.Success-> {
                    bookResource.data
                }
                is Result.Error -> {
                    null
                }
            }
            if (book != null) {
                withContext(Main){
                    service.makeBookCurrent(book)
                }
            }
            return@async book
        }
    }
}