package com.example.vbook.presentation.bookdetailed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbook.domain.common.Action
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BookDetailedVM @Inject constructor(
    private val getFilledBook: GetFilledBook
): ViewModel() {

    private val _actions: MutableStateFlow<Action> =
        MutableStateFlow(Action.idle())
    val actions: StateFlow<Action> =_actions

    fun setServiceBook(service:MediaService,title:String,author:Pair<String,String>,reader:Pair<String,String>): Deferred<Book?> {
        return viewModelScope.async(IO) {
            val book = when(val bookResource = getFilledBook(title, author, reader)){
                is Result.Success-> {
                    bookResource.data
                }
                is Result.Error -> {
                    _actions.value=Action.showToast(bookResource.message)
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