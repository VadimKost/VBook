package com.example.vbook.presentation.bookslist

import androidx.lifecycle.ViewModel
import com.example.vbook.domain.common.Action
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

    private val _actions:MutableStateFlow<Action> =
        MutableStateFlow(Action.idle())
    val  actions:StateFlow<Action> =_actions

    suspend fun loadMoreNewBooks(){
        val books= getPartOfNewBooks.invoke(page)
            when(books){
                is Resource.Success -> {
                    page+=1
                    bookList.addAll(books.data)
                    _actions.value = Action.updateRV()
                }
                is Resource.Error -> _actions.value=Action.showToast(books.message)
            }
        }
    }
