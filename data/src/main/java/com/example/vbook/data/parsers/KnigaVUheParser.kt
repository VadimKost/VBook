/*
 * Created by Vadim on 13.07.22, 12:17
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 09.07.22, 20:35
 *
 */

package com.example.vbook.data.parsers

import android.util.Log
import com.example.vbook.domain.model.Book
import org.jsoup.Jsoup
import javax.inject.Inject
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class KnigaVUheParser @Inject constructor() : BooksParser() {
    override val TAG: String="KnigaVUheParser"

    override val base_url: String="https://knigavuhe.org/"

    override fun getAllNewBooks(page: Int): MutableList<Book> {
        return parseBookList("new/?page=$page")
    }

    override fun getFilledBook(book: Book): Book {
        val url=base_url+book.bookUrl
        var jsonMp3=""
        val doc = Jsoup.connect(url).userAgent("Chrome/4.0.249.0 Safari/532.5")
            .referrer("http://www.google.com").get()
        val scripts =doc.getElementsByTag("script")
        //Todo Make more Universal
        for(i in scripts){
            val list = Regex("[\\[{].+[\\}]]").find(i.toString())?.value
            if (list != null){
                jsonMp3 =list
                Log.d("VVV",jsonMp3)
            }
        }
        jsonMp3=jsonMp3.split("]")[0]+"]"
        val gson = GsonBuilder()
            .create()
        val itemsListType = object : TypeToken<List<BookMediaParserItem>>() {}.type
        val list=gson.fromJson<List<BookMediaParserItem>>(jsonMp3,itemsListType)
        val mp3List= mutableListOf<Pair<String,String>>()
        for(i in list){
            mp3List.add(i.title to i.url)
        }
        book.mediaItems=mp3List
        Log.e("getBookDetailedPar",book.toString())
        return book
    }

    override fun search(text: String, page: Int): List<Book> {
        return parseBookList("search/?q=$text&page=$page")
    }

    override fun parseBookList(URL: String): MutableList<Book> {
        val url=base_url+URL
        val list:MutableList<Book> = mutableListOf()

        val doc = Jsoup.connect(url).userAgent("Chrome/4.0.249.0 Safari/532.5")
            .referrer("http://www.google.com").get()
        val books = doc.select(".bookkitem")

        for(element in books){
            val coverURL=element.select(".bookkitem_cover_img").attr("src")
            val title=element.select("a.bookkitem_name").text()
            val author=element.select(".bookkitem_author").select("a").text()
            val authorURL=element.select(".bookkitem_author").select("a").attr("href")
            val bookURL=element.select("a.bookkitem_name").attr("href")
            val bookkitem_meta=element.select(".bookkitem_meta")
            val reader=bookkitem_meta.select(".-reader").next().next().select("a").text()
            val readerURL=bookkitem_meta.select(".-reader").next().next().select("a").attr("href")
            val cycle=bookkitem_meta.select(".-serie").next().select("a").text()
            val cycleURL=bookkitem_meta.select(".-serie").next().select("a").attr("href")
            Log.e("PAR",(author to authorURL).toString())
            val book= Book(
                source = TAG,
                title = title,
                coverURL = coverURL,
                author = author to authorURL,
                bookUrl = bookURL,
                reader = reader to readerURL,
                cycle = cycle to cycleURL,
                isFavorite = false
            )
            list.add(book)
        }
        return list
    }

    private data class  BookMediaParserItem(
        val id : Int,
        val title : String,
        val url : String,
        val error : Int,
        val duration : Int,
        val duration_float : Double
    )
}