package com.example.vbook.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.vbook.domain.model.Book

@Entity(tableName = "BookEntity",primaryKeys = ["title", "author"])
@TypeConverters(TypeConvertor::class)
data class BookEntity(
    @ColumnInfo(name = "source") var source:String,
    @ColumnInfo(name = "bookURL") var bookURL:String,
    @ColumnInfo(name = "title") var title:String,
    @ColumnInfo(name = "author") var author:Pair<String,String>,
    @ColumnInfo(name = "reader") var reader:Pair<String,String>,
    @ColumnInfo(name = "coverURL") var coverURL:String,
    @ColumnInfo(name = "mp3List") var mp3List: List<Pair<String,String>>?=null,
    @ColumnInfo(name = "cycle") val cycle:Pair<String,String>,
    @ColumnInfo(name = "cycleBookList") var cycleBookList: List<Pair<String,String>>?=null,
    @ColumnInfo(name = "duration") var duration: String?=null,
    @ColumnInfo(name = "isCurrent") var isCurrent:Boolean,
    @ColumnInfo(name = "stoppedTrackIndex")var stoppedTrackIndex:Int,
    @ColumnInfo(name = "stoppedTrackTime")var stoppedTrackTime: Float,

){
    companion object{
        fun toBook(bookEntity:BookEntity): Book {
            return Book(
                bookEntity.source,
                bookEntity.bookURL,
                bookEntity.title,
                bookEntity.author,
                bookEntity.reader,
                bookEntity.coverURL,
                bookEntity.mp3List,
                bookEntity.cycle,
                bookEntity.cycleBookList,
                bookEntity.duration,
                bookEntity.isCurrent,
                bookEntity.stoppedTrackIndex,
                bookEntity.stoppedTrackTime
            )
        }

        fun fromBook(book: Book): BookEntity {
            return BookEntity(
                book.source,
                book.bookURL,
                book.title,
                book.author,
                book.reader,
                book.coverURL,
                book.mp3List,
                book.cycle,
                book.cycleBookList,
                book.duration,
                book.isCurrent,
                book.stoppedTrackIndex,
                book.stoppedTrackTime
            )
        }

        fun toBookList(bookEntityList:List<BookEntity>): List<Book> {
            val bookList= mutableListOf<Book>()
            for (bookEntity in bookEntityList){
                bookList.add(Book(
                    bookEntity.source,
                    bookEntity.bookURL,
                    bookEntity.title,
                    bookEntity.author,
                    bookEntity.reader,
                    bookEntity.coverURL,
                    bookEntity.mp3List,
                    bookEntity.cycle,
                    bookEntity.cycleBookList,
                    bookEntity.duration,
                    bookEntity.isCurrent,
                    bookEntity.stoppedTrackIndex,
                    bookEntity.stoppedTrackTime
                ))
            }
            return bookList
        }

        fun fromBookList(bookList: List<Book>): List<BookEntity> {
            val bookEntityList= mutableListOf<BookEntity>()
            for (book in bookList){
                bookEntityList.add(BookEntity(
                    book.source,
                    book.bookURL,
                    book.title,
                    book.author,
                    book.reader,
                    book.coverURL,
                    book.mp3List,
                    book.cycle,
                    book.cycleBookList,
                    book.duration,
                    book.isCurrent,
                    book.stoppedTrackIndex,
                    book.stoppedTrackTime
                ))
            }

            return bookEntityList
        }
    }

}

