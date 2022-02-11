package com.example.vbook.data.mapper

import com.example.vbook.data.db.model.BookEntity
import com.example.vbook.domain.model.Book

fun Book.mapToData() = BookEntity(
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

fun List<Book>.mapToData() = this.map { it.mapToData() }

fun BookEntity.mapToDomain() = Book(
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

fun List<BookEntity>.mapToDomain() = this.map { it.mapToDomain() }

