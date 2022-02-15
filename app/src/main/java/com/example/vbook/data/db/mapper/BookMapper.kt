package com.example.vbook.data.db.mapper

import com.example.vbook.data.db.model.BookEntity
import com.example.vbook.common.model.Book

fun Book.toBookEntity()= BookEntity(
    source,
    bookURL,
    title,
    author,
    reader,
    coverURL,
    mp3List,
    cycle,
    cycleBookList,
    duration,
    isCurrent,
    stoppedTrackIndex,
    stoppedTrackTime
)

fun List<Book>.toBookEntityList() = this.map { it.toBookEntity() }

fun BookEntity.toBook()= Book(
    source,
    bookURL,
    title,
    author,
    reader,
    coverURL,
    mp3List,
    cycle,
    cycleBookList,
    duration,
    isCurrent,
    stoppedTrackIndex,
    stoppedTrackTime
)

fun List<BookEntity>.toBookList() = this.map { it.toBook() }

