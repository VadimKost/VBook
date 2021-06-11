package com.example.vbook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vbook.data.model.Book
import com.example.vbook.databinding.BookItemBinding

class BookSetAdapterRV(var books: MutableList<Book>,val onItemClicked:(Int)->Unit): RecyclerView.Adapter<BookSetAdapterRV.LinkHolder>() {
    inner class LinkHolder(val binding: BookItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.book=books.toList().get(position)
            binding.executePendingBindings()
            binding.root.setOnClickListener{
                onItemClicked(position)
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
