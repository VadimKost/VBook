package com.example.vbook.presentation.bookslist

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vbook.data.repository.BookRepositoryImpl
import com.example.vbook.domain.common.InMemoryStorage
import com.example.vbook.domain.common.Resurce
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
    val inMemoryStorage: InMemoryStorage
):ViewModel() {
    var page=1
    var bookList= inMemoryStorage.books

    private val _actions:MutableStateFlow<ActionAndState> =
        MutableStateFlow(ActionAndState.updateRV())

    val  actions:StateFlow<ActionAndState> =_actions

    init {
        runBlocking {
            loadMoreNewBooks()
        }
    }

    suspend fun loadMoreNewBooks(){
        Log.e("VVV",page.toString())
        Log.e("VVV",bookList.toString())
        getPartOfNewBooks.execute(page++).collect {
            when(it){
                is Resurce.Success -> _actions.value=ActionAndState.updateRV()
                is Resurce.Error -> _actions.value=ActionAndState.showToast(it.message)
            }
        }
    }

     sealed class ActionAndState{
         class updateRV:ActionAndState()
         class showToast(val message:String):ActionAndState()
     }
}