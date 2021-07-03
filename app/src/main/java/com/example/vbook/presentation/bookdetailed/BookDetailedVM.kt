package com.example.vbook.presentation.bookdetailed

import androidx.lifecycle.ViewModel
import com.example.vbook.domain.model.Book
import com.example.vbook.domain.common.Resurce
import com.example.vbook.domain.usecases.GetBookDetailed
import com.example.vbook.presentation.bookslist.BooksListVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class BookDetailedVM @Inject constructor(
    val getBookDetailed: GetBookDetailed,
): ViewModel() {
//    val bookList= inMemoryStorage.books

    private val _actions: MutableStateFlow<BooksListVM.ActionAndState> =
        MutableStateFlow(BooksListVM.ActionAndState.updateRV())

    val  actions: StateFlow<BooksListVM.ActionAndState> =_actions


    suspend fun getBookDetailed(book:Book):Book?{
        var result: Book? =null
        getBookDetailed.execute(book).collect {
                when(it){
                    is Resurce.Success -> result=it.data
                    is Resurce.Error ->
                        _actions.value= BooksListVM.ActionAndState.showToast(it.message)
                }
            }
        return result
        }


    sealed class ActionAndState{
        class updateRV:ActionAndState()
        class showToast(val message:String):ActionAndState()
    }
}