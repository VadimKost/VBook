package com.example.vbook.ui.bookslist

import androidx.lifecycle.ViewModel
import com.example.vbook.data.model.Book
import com.example.vbook.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class BooksListVM @Inject constructor(
    val booksRepository: BookRepository
):ViewModel() {
    var page=1
    var newBooks= booksRepository.newBookData

    private val _actions= MutableStateFlow(Action.updateRV())
    val  actions:StateFlow<Action> =_actions

    init {
        runBlocking {
            loadMoreNewBooks()
        }
    }

    suspend fun loadMoreNewBooks(){
        booksRepository.receiveNewBook(page++)
        _actions.value=Action.updateRV()
    }

     sealed class Action{
         class updateRV:Action()
     }
}