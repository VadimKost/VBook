package com.example.vbook.data.parsers

import android.util.Log
import com.example.vbook.data.model.Book
import org.jsoup.Jsoup
import javax.inject.Inject
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class KnigaVUheParser @Inject constructor() : BooksParser() {
    companion object{
        val TAG: String="KnigaVUheParser"
    }
    override val base_url: String="https://knigavuhe.org/"


    override fun getAllBookList(page: Int): MutableList<Book> {
        return parseBookList("new/?page=$page")
    }

    override fun getBookDetailed(book: Book): Book {
        val url=base_url+book.bookURL
        var mp3List=""
        val doc = Jsoup.connect(url).userAgent("Chrome/4.0.249.0 Safari/532.5")
            .referrer("http://www.google.com").get()
        val scripts =doc.getElementsByTag("script")
        for(i in scripts){
            val list = Regex("[\\[{].+[\\}]]").find(i.toString())?.value
            if (list != null){
                mp3List =list
            }
        }
        val gson = GsonBuilder()
            .setLenient()
            .create();
        val itemsListType = object : TypeToken<List<BookDetailedFragments>>() {}.type
        val list=gson.fromJson<BookDetailedFragments>(mp3List,itemsListType)
        return book
    }


    override fun search(text: String, page: Int): List<Book> {
        TODO("Not yet implemented")
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

            val book= Book(
                source= TAG,
                title = title,
                coverURL = coverURL,
                author = author to authorURL,
                bookURL = bookURL,
                reader = reader to readerURL,
                cycle = cycle to cycleURL
            )
            list.add(book)
        }
        return list
    }

    data class  BookDetailedFragments(
        val id : Int,
        val title : String,
        val url : String,
        val error : Int,
        val duration : Int,
        val duration_float : Double
    )
}