package com.example.vbook.data.parsers

import android.util.Log
import com.example.vbook.data.model.Book
import org.jsoup.Jsoup
import javax.inject.Inject


class KnigaVUheParser @Inject constructor() : BooksParser() {
    override val base_url: String="https://knigavuhe.org/"

    override fun getAllBookList(page: Int): MutableList<Book> {
        return parseBookList("new/?page=$page")
    }

    override fun getBookDetailed(book: Book): Book {
        val url=base_url+book.bookURL
        val doc = Jsoup.connect(url).userAgent("Chrome/4.0.249.0 Safari/532.5")
            .referrer("http://www.google.com").get()
        val script =doc.getElementsByTag("script").first()
        val list = Regex("[\\[].+[\\]]").find(script.text())?.value
        Log.e("VVV",list.toString())
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
}