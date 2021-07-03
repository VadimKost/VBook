package com.example.vbook.data.db.dao

import androidx.room.*
import com.example.vbook.data.db.model.BookEntity
import com.example.vbook.data.db.model.TypeConvertor
import com.example.vbook.domain.model.Book
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

@Dao
@TypeConverters(TypeConvertor::class)
interface BookDAO{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(books: List<BookEntity>)

    @Query("SELECT * FROM BookEntity")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM BookEntity WHERE isCurrent")
    fun getCurrentBook(): Flow<BookEntity>

    @Query("SELECT * FROM BookEntity WHERE title=:title AND author=:author")
    fun getBook(title:String,author:Pair<String,String>): Flow<BookEntity>

    @Update
    fun updateBook(book: BookEntity):Int

    @Transaction
    suspend fun replaceCurrentBook(newBook: BookEntity){
        getCurrentBook().collect {
            updateBook(it.apply { isCurrent=false })
            updateBook(newBook.also { it.isCurrent=true })
        }
    }



}