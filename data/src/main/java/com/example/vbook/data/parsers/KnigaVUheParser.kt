/*
 * Created by Vadim on 13.07.22, 12:17
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 09.07.22, 20:35
 *
 */

package com.example.vbook.data.parsers

import android.util.Log
import com.example.vbook.domain.book.model.Author
import com.example.vbook.domain.book.model.Book
import com.example.vbook.domain.book.model.Cycle
import com.example.vbook.domain.book.model.Reader
import com.example.vbook.domain.book.model.Voiceover
import com.example.vbook.domain.shared.model.MediaItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.UUID
import javax.inject.Inject

//class KnigaVUheParser @Inject constructor() : BooksParser() {
//    val TAG: String = "KnigaVUheParser"
//
//    override val source: Source = Source.KnigaVUhe
//
//    override suspend fun getNewBooks(page: Int): MutableList<Book> {
//        return parseBookList("new/?page=$page")
//    }
//
//    override suspend fun search(text: String, page: Int): List<Book> {
//        return parseBookList("search/?q=$text&page=$page")
//    }
//
//    fun parseBookList(url: String): MutableList<Book> {
//        val url = source.baseUrl + url
//        val list: MutableList<Book> = mutableListOf()
//
//        val doc = Jsoup.connect(url).userAgent("Chrome/4.0.249.0 Safari/532.5")
//            .referrer("http://www.google.com").get()
//        val books = doc.select(".bookkitem")
//
//        for (element in books) {
//            val coverURL = element.select(".bookkitem_cover_img").attr("src")
//            val title = element.select("a.bookkitem_name").text()
//            val author = element.select(".bookkitem_author").select("a").text()
//            val authorURL = element.select(".bookkitem_author").select("a").attr("href")
//            val bookURL = element.select("a.bookkitem_name").attr("href")
//            val bookkitem_meta = element.select(".bookkitem_meta")
//            val reader = bookkitem_meta.select(".-reader").next().next().select("a").text()
//            val readerURL = bookkitem_meta.select(".-reader").next().next().select("a").attr("href")
//            val cycle = bookkitem_meta.select(".-serie").next().select("a").text()
//            val cycleURL = bookkitem_meta.select(".-serie").next().select("a").attr("href")
//            Log.e("PAR", (author to authorURL).toString())
//            val book = Book(
//                source = TAG,
//                title = title,
//                coverUrl = coverURL,
//                author = author to authorURL,
//                bookUrl = bookURL,
//                reader = reader to readerURL,
//                cycle = cycle to cycleURL,
//                isFavorite = false
//            )
//            list.add(book)
//        }
//        return list
//    }
//
//    override suspend fun getBookByAuthorAndTitle(author: String, title: String): Book? {
//        return withContext(Dispatchers.IO) {
//            val searchQuery = "$author+$title".replace(" ", "+")
//            val searchResult = search(searchQuery, 0)
//            val result = searchResult.first { it.authors.any { author } && it.title = title }
//
//            val gson = GsonBuilder()
//                .create()
//            val parseResultListType = object : TypeToken<List<ParseResult>>() {}.type
//
//            val doc = Jsoup.connect(url).userAgent("Chrome/4.0.249.0 Safari/532.5")
//                .referrer("http://www.google.com").get()
//            val scripts = doc.getElementsByTag("script")
//
//            val regexResult = scripts.map { part ->
//                Regex("""var player = new BookPlayer\([^,]+, (\[\{.*?\}\])""")
//                    .find(part.toString())?.groupValues[1]
//            }.filterNotNull().first()
//
//            val parseResults = gson.fromJson<List<ParseResult>>(regexResult, parseResultListType)
//            book.mediaItems = parseResults.map {
//                it.title to it.url
//            }
//
//            Log.e("getBookDetailedPar", book.toString())
//            book
//        }
//    }
//
//
//    suspend fun searchVoiceovers(bookInternalIds: List<String>): List<Voiceover> {
//        withContext(Dispatchers.IO) {
//            val url = source.baseUrl + bookInternalId
//            val gson = GsonBuilder()
//                .create()
//            val parseResultListType = object : TypeToken<List<ParseResult>>() {}.type
//
//            val doc = Jsoup.connect(url).userAgent("Chrome/4.0.249.0 Safari/532.5")
//                .referrer("http://www.google.com").get()
//            val scripts = doc.getElementsByTag("script")
//
//            val regexResult = scripts.map { part ->
//                Regex("""var player = new BookPlayer\([^,]+, (\[\{.*?\}\])""")
//                    .find(part.toString())?.groupValues[1]
//            }.filterNotNull().first()
//
//            val parseResults = gson.fromJson<List<ParseResult>>(regexResult, parseResultListType)
//
//            book.mediaItems = parseResults.map {
//                it.title to it.url
//            }
//
//            Log.e("getBookDetailedPar", book.toString())
//            book
//        }
//    }
//
//
//}

// TODO: Think to return internal id for getting details or
//  other mechanisms like plain search and filtering to simplify DB

// TODO: Optimize search

// TODO: Voiceover id return
class KnigaVUheParser @Inject constructor(private val gson: Gson) : BooksParser() {

    override val source: Source = Source.KnigaVUhe

    override suspend fun getNewBooks(page: Int): Map<String, Book> {
        val url = "${source.baseUrl}/new/?page=$page"
        val newBooksPageDocument = Jsoup.connect(url)
            .userAgent("Chrome/4.0.249.0 Safari/532.5")
            .referrer("http://www.google.com")
            .get()

        return parseBookList(newBooksPageDocument)
    }

    override suspend fun getBookDetails(internalId: String): List<Voiceover> = coroutineScope {
        Log.e("TAG", "asd")
        val url = "${source.baseUrl}/book/$internalId/"
        val bookPageDocument = Jsoup.connect(url)
            .userAgent("Chrome/4.0.249.0 Safari/532.5")
            .referrer("http://www.google.com")
            .get()

        val otherVoiceoversIds = bookPageDocument.selectFirst(".book_serie_block")?.let {
            it.select(".book_serie_block_item")
            .select("a")
            .map { it.attr("href") }
            .filter { it.contains("book") }
            .map { it.split("/")[2] }
        }


        Log.e("asd", otherVoiceoversIds.toString())
        val voiceovers = (otherVoiceoversIds?.map {
            async {
                val voiceoverUrl = "${source.baseUrl}/book/$it/"
                val voiceoverDocument = Jsoup.connect(voiceoverUrl)
                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("http://www.google.com")
                    .get()
                parseVoiceover(voiceoverDocument)
            }
        }?.awaitAll() ?: emptyList()) + parseVoiceover(bookPageDocument)

        Log.e("asd", voiceovers.toString())

        return@coroutineScope voiceovers
    }


    override suspend fun getBooksInCycle(internalId: String): Map<String, Book> {
        val url = "${source.baseUrl}/serie/$internalId/"
        val bookListDocument = Jsoup.connect(url)
            .userAgent("Chrome/4.0.249.0 Safari/532.5")
            .referrer("http://www.google.com")
            .get()
        return parseBookList(bookListDocument)
    }


    private fun parseBookList(document: Document): Map<String, Book> {
        val bookItem = document.select(".bookkitem")

        return bookItem.map { elements ->
            val authors = elements.select(".bookkitem_author")
                .select("a")
                .map { Author(fullName = it.text()) }

            val bookItemMeta = elements.select(".bookkitem_meta")

            val readers = bookItemMeta.select(".-reader").next().next().select("a")
                .map { Reader(fullName = it.text()) }.filter { it.fullName.isNotEmpty() }

            val voiceover = Voiceover(
                readers = readers,
                mediaItems = listOf(),
            )

            val title = elements.select("a.bookkitem_name").text()
            val internalId = elements.select("a.bookkitem_name").attr("href").split("/")[2]

            val cover = elements.select(".bookkitem_cover_img").attr("src")

            val cycleName = bookItemMeta.select(".-serie").next().select("a").text()

            internalId to Book(
                inAppId = generateLocalBookUUID(title, authors),
                title = title,
                cover = cover,
                authors = authors,
                voiceovers = listOf(voiceover),
                cycle = Cycle(
                    name = cycleName,
                    numberInCycle = -1
                )
            )
        }.toMap()
    }

    private fun parseVoiceover(document: Document): Voiceover {
        val scripts = document.getElementsByTag("script")

        val mediaItemsRegexResult = scripts.firstNotNullOf { part ->
            Regex("""var player = new BookPlayer\([^,]+, (\[\{.*?\}\])""")
                .find(part.toString())?.groupValues?.get(1)
        }

        val parseResultType = object : TypeToken<List<VoiceoverParseResult>>() {}.type
        val parseResult =
            gson.fromJson<List<VoiceoverParseResult>>(mediaItemsRegexResult, parseResultType)


        val mediaItems = parseResult.map {
            MediaItem(
                uri = it.url,
                title = it.title,
                duration = it.duration,
            )
        }

        val readers = document.select(".book_title_block")
            .select("a")
            .filter { it.attr("href").contains("reader") }
            .map { Reader(it.text()) }

        val voiceover = Voiceover(readers = readers, mediaItems = mediaItems)


        return voiceover
    }

    //TODO: think about
    fun generateLocalBookUUID(title: String, authors: List<Author>): String {
        val input = source.baseUrl + title + authors.joinToString()
        return UUID.nameUUIDFromBytes(input.toByteArray()).toString()
    }

}

private data class VoiceoverParseResult(
    val id: Int,
    val title: String,
    val url: String,
    val error: Int,
    val duration: Long,
)