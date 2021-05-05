package com.example.vbook.ui.bookslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.vbook.data.parsers.KnigaVUheParser
import com.example.vbook.databinding.FragmentBookListBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class BooksListFragment : Fragment() {
    lateinit var binding:FragmentBookListBinding
    val vm: BooksListVM by lazy {
        ViewModelProvider(this).get(BooksListVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentBookListBinding.inflate(inflater,container,false)
        GlobalScope.launch(Dispatchers.IO) {
            var book=KnigaVUheParser(KnigaVUheParser.BASE_URL).getBooks(1).get(0)
            withContext(Dispatchers.Main){
                Picasso.with(context).load(book.coverURL).into(binding.imageView)
                binding.textView.text=book.title
            }
        }


        return binding.root
    }
}