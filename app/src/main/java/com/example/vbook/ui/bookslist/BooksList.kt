package com.example.vbook.ui.bookslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.vbook.databinding.FragmentBookListBinding


class BooksList : Fragment() {
    lateinit var binding:FragmentBookListBinding
    val vm: BooksListVM by lazy {
        ViewModelProvider(this).get(BooksListVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentBookListBinding.inflate(inflater,container,false)

        return binding.root
    }
}