package com.example.vbook.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vbook.domain.model.Book
import com.example.vbook.databinding.BookItemBinding

class BookSetAdapterRV(var books: MutableList<Book>,val onItemClicked:(String,Pair<String,String>,Pair<String,String>)->Unit): RecyclerView.Adapter<BookSetAdapterRV.LinkHolder>() {
    inner class LinkHolder(val binding: BookItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            val book = books.get(position)
            Log.e("VVV",book.toString())
            binding.book=book
            binding.executePendingBindings()
            binding.root.setOnClickListener{
                onItemClicked(book.title,book.author,book.reader)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BookItemBinding.inflate(inflater)
        return LinkHolder(binding)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: LinkHolder, position: Int) {
        holder.bind(position)
    }
}
