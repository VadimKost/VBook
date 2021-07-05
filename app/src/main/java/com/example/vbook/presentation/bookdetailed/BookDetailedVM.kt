package com.example.vbook.presentation.bookdetailed

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vbook.domain.common.ActionAndState
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.common.Resource
import com.example.vbook.domain.usecases.GetBook
import com.example.vbook.domain.usecases.GetBookDetailed
import com.example.vbook.domain.usecases.MakeBookCurrent
import com.example.vbook.isDetailed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BookDetailedVM @Inject constructor(
    private val getDetailedBook: GetBookDetailed,
    private val makeBookCurrent: MakeBookCurrent,
    private val getBook: GetBook
): ViewModel() {

    private val _actions: MutableStateFlow<ActionAndState> =
        MutableStateFlow(ActionAndState.idle())
    val actions: StateFlow<ActionAndState> =_actions

    private val _book: MutableStateFlow<Book?> = MutableStateFlow(null)
    val  book: StateFlow<Book?> =_book

    suspend fun getBookDetailed(title:String,author:Pair<String,String>){
        val bookEntity = getBook(title, author)
        when(bookEntity){
            is Resource.Success -> {
                if (bookEntity.data.isDetailed()){
                    makeBookCurrent(bookEntity.data)
                    _book.value=bookEntity.data
                    Log.e("bookEntityD",bookEntity.data.toString())
                }else{
                    getDetailedBook(bookEntity.data)
                    makeBookCurrent(bookEntity.data)
                    _book.value=bookEntity.data
                    Log.e("bookEntity",bookEntity.data.toString())
                }
            }
            is Resource.Error -> {
                _actions.value=ActionAndState.showToast(bookEntity.message)
            }
        }
    }
}