package com.example.vbook.presentation.bookslist

import androidx.lifecycle.ViewModel
import com.example.vbook.domain.common.ActionAndState
import com.example.vbook.domain.common.Resource
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.usecases.GetPartOfNewBooks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BooksListVM @Inject constructor(
    val getPartOfNewBooks: GetPartOfNewBooks,
):ViewModel() {
    var page=1
    var bookList= mutableListOf<Book>()

    private val _actions:MutableStateFlow<ActionAndState> =
        MutableStateFlow(ActionAndState.idle())
    val  actions:StateFlow<ActionAndState> =_actions

    suspend fun loadMoreNewBooks(){
        val books= getPartOfNewBooks.invoke(page++)
            when(books){
                is Resource.Success -> {
                    bookList.addAll(books.data)
                    _actions.value = ActionAndState.updateRV()
                }
                is Resource.Error -> _actions.value=ActionAndState.showToast(books.message)
            }
        }
    }
