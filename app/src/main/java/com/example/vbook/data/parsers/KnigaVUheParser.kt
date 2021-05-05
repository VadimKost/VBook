package com.example.vbook.data.parsers

import android.util.Log
import com.example.vbook.data.model.Book
import org.jsoup.Jsoup
import javax.inject.Inject


class KnigaVUheParser @Inject constructor(override var base_url: String) : BooksParser() {
    companion object{
        var BASE_URL="https://knigavuhe.org/"
    }

    override fun getBooks(page: Int): MutableList<Book> {
        val url=base_url+"new/?page=$page"
        val list:MutableList<Book> = mutableListOf()

        val doc = Jsoup.connect(url).userAgent("Chrome/4.0.249.0 Safari/532.5")
            .referrer("http://www.google.com").get()
        val books = doc.select(".bookkitem")

        for(element in books){
            val coverURL=element.select(".bookkitem_cover_img").attr("src")
            val title=element.select("a.bookkitem_name").text()
            val author=element.select(".bookkitem_author").select("a").text()
            val authorURL=element.select(".bookkitem_author").select("a")
                .attr("href")
            val reader=element.select(".bookkitem_meta_block")[0]
                .select("span")[1].text()
            val readerURL=element.select(".bookkitem_meta_block")[0]
                .select("a").attr("href")
            val bookURL=element.select("a.bookkitem_name").attr("href")
//            duration
//            cycle

            val book= Book(
                title = title,
                coverURL = coverURL,
                author = author to authorURL,
                bookURL = bookURL,
                reader = reader to readerURL)

            list.add(book)
        }


        return list
    }

    override fun search(text: String, page: Int): List<Book> {
        TODO("Not yet implemented")
    }
}