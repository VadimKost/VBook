package com.example.vbook.presentation.bookslist

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vbook.domain.common.Resurce
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.usecases.GetPartOfNewBooks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class BooksListVM @Inject constructor(
    val getPartOfNewBooks: GetPartOfNewBooks,
):ViewModel() {
    var page=1
    var bookList= mutableListOf<Book>()

    private val _actions:MutableStateFlow<ActionAndState> =
        MutableStateFlow(ActionAndState.updateRV())
    val  actions:StateFlow<ActionAndState> =_actions

    init {
        runBlocking {
            loadMoreNewBooks()
        }
    }

    suspend fun loadMoreNewBooks(){
        getPartOfNewBooks.execute(page++).collect {
            when(it){
                is Resurce.Success -> {
                    _actions.value = ActionAndState.updateRV()
                    bookList.addAll(it.data)
                }
                is Resurce.Error -> _actions.value=ActionAndState.showToast(it.message)
            }
        }
    }

     sealed class ActionAndState{
         class updateRV:ActionAndState()
         class showToast(val message:String):ActionAndState()
     }
}