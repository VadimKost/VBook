package com.example.vbook.data.db.dao

import androidx.room.*
import com.example.vbook.data.db.model.BookEntity
import com.example.vbook.data.db.model.TypeConvertor
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(TypeConvertor::class)
interface BookDAO{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(books: List<BookEntity>)

    @Query("SELECT * FROM BookEntity")
    suspend fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM BookEntity WHERE isCurrent=1")
    suspend fun getCurrentBook(): BookEntity?

    @Query("SELECT * FROM BookEntity WHERE bookURL=:bookUrl")
    suspend fun getBook(bookUrl:String): BookEntity?

    @Update
    suspend fun updateBook(book: BookEntity):Int

}