package com.example.vbook.ui.bookdetailed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.vbook.R
import com.example.vbook.databinding.FragmentBookDetailedBinding
import com.example.vbook.databinding.FragmentBookListBinding
import com.example.vbook.ui.bookslist.BooksListVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailedFragment : Fragment() {
    lateinit var binding:FragmentBookDetailedBinding

    val vm: BookDetailedVM by lazy {
        ViewModelProvider(this).get(BookDetailedVM::class.java)
    }

    val args: BookDetailedFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBookDetailedBinding.inflate(inflater,container,false)
        binding.textView.text=args.bookIndex.toString()
        return binding.root
    }

}