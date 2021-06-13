package com.example.vbook.ui.bookslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vbook.adapter.BookSetAdapterRV
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

        val adapterRV =BookSetAdapterRV(vm.bookList){
            val action = BooksListFragmentDirections
                .actionBookListToBookDetailedFragment(bookIndex = it)
            findNavController().navigate(action)
        }
        binding.rv.adapter=adapterRV
        binding.rv.layoutManager=LinearLayoutManager(context)

        lifecycleScope.launch {
            vm.actions.collect{
                when(it){
                    is BooksListVM.Action.updateRV ->adapterRV.notifyDataSetChanged()
                }
            }
        }
        binding.fab.setOnClickListener {
            lifecycleScope.launch {
                vm.loadMoreNewBooks()
            }
        }

        return binding.root
    }
}