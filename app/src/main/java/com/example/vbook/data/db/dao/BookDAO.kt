package com.example.vbook.data.db.dao

import androidx.room.*
import com.example.vbook.data.db.model.BookEntity
import com.example.vbook.data.db.TypeConvertor

@Dao
@TypeConverters(TypeConvertor::class)
interface BookDAO{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(books: List<BookEntity>)

    @Query("SELECT * FROM BookEntity")
    suspend fun getAllBooks(): List<BookEntity>

    @Query("SELECT * FROM BookEntity WHERE bookURL=:bookUrl")
    suspend fun getBook(bookUrl:String): BookEntity?

    @Query("SELECT * FROM BookEntity WHERE isFavorite=1")
    suspend fun getFavoriteBooks(): List<BookEntity>

    @Query("UPDATE BookEntity SET isFavorite = :isFavorite WHERE bookURL=:bookUrl")
    suspend fun setFavoriteBook(bookUrl: String,isFavorite:Boolean)

    @Query(
        "UPDATE BookEntity " +
                "SET stoppedTrackTime = :stoppedTrackIndex,stoppedTrackTime = :stoppedTrackTime " +
                "WHERE bookURL=:bookUrl")
    suspend fun setBookTimeLine(bookUrl: String, stoppedTrackIndex:Int, stoppedTrackTime:Long)


}