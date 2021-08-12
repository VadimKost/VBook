package com.example.vbook.presentation.bookslist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vbook.presentation.adapter.BookSetAdapterRV
import com.example.vbook.databinding.FragmentBookListBinding
import com.example.vbook.domain.common.Action
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
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
        lifecycleScope.launch(Dispatchers.IO){
            vm.loadMoreNewBooks()
        }
        val adapterRV =BookSetAdapterRV(vm.bookList){title,author,reader ->
            val action = BooksListFragmentDirections
                .actionBookListToBookDetailedFragment(title,
                    author.first,author.second,
                    reader.first,reader.second)
            findNavController().navigate(action)
        }
        binding.rv.adapter=adapterRV
        binding.rv.layoutManager=LinearLayoutManager(context)

        lifecycleScope.launch {
            vm.actions.collect{
                when(it){
                    is Action.updateRV ->adapterRV.notifyDataSetChanged()
                    is Action.showToast-> Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.fab.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                vm.loadMoreNewBooks()
            }
        }

        return binding.root
    }
}