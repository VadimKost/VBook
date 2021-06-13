package com.example.vbook.ui.bookdetailed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.vbook.R
import com.example.vbook.databinding.FragmentBookDetailedBinding
import com.example.vbook.databinding.FragmentBookListBinding
import com.example.vbook.ui.bookslist.BooksListVM
import com.google.android.exoplayer2.MediaItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.google.android.exoplayer2.SimpleExoPlayer

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
        val player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.videoView.setPlayer(player)
        lifecycleScope.launch {
            val mediaItemList= mutableListOf<MediaItem>()
            for(i in vm.getBookDetailed(vm.bookList.get(args.bookIndex)).mp3List!!){
                mediaItemList.add(MediaItem.fromUri(i.second))
            }
            player.setMediaItems(mediaItemList)
        }
        player.prepare();
        player.play();

        return binding.root
    }

}