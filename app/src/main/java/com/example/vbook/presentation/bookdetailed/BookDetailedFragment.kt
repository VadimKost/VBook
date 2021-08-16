package com.example.vbook.presentation.bookdetailed

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.vbook.databinding.FragmentBookDetailedBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import com.example.vbook.presentation.mediaservice.MediaService
import com.example.vbook.domain.common.Action
import com.example.vbook.domain.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class BookDetailedFragment : Fragment() {
    var playerServiceBinder: MediaService.PlayerServiceBinder? = null

    lateinit var binding:FragmentBookDetailedBinding

    lateinit var vm: BookDetailedVM



    val args: BookDetailedFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vm=ViewModelProvider(this).get(BookDetailedVM::class.java)
        binding= FragmentBookDetailedBinding.inflate(inflater,container,false)

        setActionListener()

        context?.bindService(Intent(context, MediaService::class.java),object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                playerServiceBinder = service as MediaService.PlayerServiceBinder
                val service = playerServiceBinder!!.service

                vm.setServiceBook(
                    service,
                    args.title,
                    args.author to args.authorURL,
                    args.reader to args.readerURL
                )
                setPlayerControlling(service)
                lifecycleScope.launch {
                    service.currentBook.collect {
                        if (it != null){
                            binding.titleD.text=it.title
                        }
                    }
                }
                lifecycleScope.launch {
                    service.trackIndex.collect {
                        binding.indexD.text=it.toString()
                    }
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                playerServiceBinder = null;
            }

        },BIND_AUTO_CREATE)


        return binding.root
    }

    fun setActionListener(){
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){
                vm.actions.collect{
                    when(it){
                        is Action.showToast-> Toast.makeText(
                            requireContext(),
                            it.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    fun setPlayerControlling(service: MediaService){
        val player=service.player
        binding.play.setOnClickListener {
            player.playWhenReady =true
        }
        binding.pause.setOnClickListener {
            player.playWhenReady =false
        }
    }

}