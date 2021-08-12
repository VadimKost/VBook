package com.example.vbook.presentation.bookdetailed

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vbook.domain.common.Action
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.common.Resource
import com.example.vbook.domain.usecases.GetFilledBook
import com.example.vbook.domain.usecases.MakeBookCurrent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BookDetailedVM @Inject constructor(
    private val fillBook: GetFilledBook,
    private val makeBookCurrent: MakeBookCurrent,
    private val getFilledBook: GetFilledBook
): ViewModel() {

    private val _actions: MutableStateFlow<Action> =
        MutableStateFlow(Action.idle())
    val actions: StateFlow<Action> =_actions

    private val _book: MutableStateFlow<Book?> = MutableStateFlow(null)
    val  book: StateFlow<Book?> =_book

    suspend fun setCurrentBook(title:String, author:Pair<String,String>, reader:Pair<String,String>){
        val bookResource = getFilledBook(title, author, reader)
        when(bookResource){
            is Resource.Success -> {
                makeBookCurrent(bookResource.data)
            }
            is Resource.Error -> {
                _actions.value=Action.showToast(bookResource.message)
            }
        }
    }
}