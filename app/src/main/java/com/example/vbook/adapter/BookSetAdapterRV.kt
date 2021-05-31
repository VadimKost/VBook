package com.example.vbook.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.vbook.data.model.Book
import com.example.vbook.data.parsers.KnigaVUheParser
import com.example.vbook.data.repository.BookRepository
import com.example.vbook.databinding.BookItemBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class BookSetAdapterRV(var books: Set<Book>): RecyclerView.Adapter<BookSetAdapterRV.LinkHolder>() {
    inner class LinkHolder(val binding: BookItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.book=books.toList().get(position)
            binding.executePendingBindings()
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
