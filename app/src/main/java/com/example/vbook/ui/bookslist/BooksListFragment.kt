package com.example.vbook.ui.bookslist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.vbook.data.parsers.KnigaVUheParser
import com.example.vbook.data.repository.BookRepository
import com.example.vbook.databinding.FragmentBookListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

        lifecycleScope.launch {
            vm.allBooks.collect {
                Log.e("AAA",it.toString())
                binding.textView.text=it.toString()
            }
        }

            binding.button.setOnClickListener {
                lifecycleScope.launch {vm.loadMoreBooks()}
            }



        return binding.root
    }
}