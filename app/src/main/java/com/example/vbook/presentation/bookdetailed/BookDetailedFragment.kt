package com.example.vbook.presentation.bookdetailed

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.vbook.databinding.FragmentBookDetailedBinding
import com.example.vbook.presentation.bookslist.BooksListVM
import com.google.android.exoplayer2.MediaItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.coroutines.flow.collect
import android.support.v4.media.session.MediaControllerCompat
import com.example.vbook.data.mediaservice.MediaService
import android.support.v4.media.session.PlaybackStateCompat

import com.example.vbook.presentation.MainActivity





@AndroidEntryPoint
class BookDetailedFragment : Fragment() {
    var playerServiceBinder: MediaService.PlayerServiceBinder? = null
    var mediaController: MediaControllerCompat? = null

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

        context?.bindService(Intent(context,MediaService::class.java),object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                playerServiceBinder = service as MediaService.PlayerServiceBinder
                try {
                    mediaController = MediaControllerCompat(
                        context, playerServiceBinder!!.mediaSessionToken
                    )
                    mediaController!!.registerCallback(
                        object : MediaControllerCompat.Callback() {
                            override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
                                if (state == null) return
                            }
                        }
                    )
                } catch (e: RemoteException) {
                    mediaController = null
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                playerServiceBinder = null;
                mediaController = null;
            }

        },BIND_AUTO_CREATE)

        binding.play.setOnClickListener {
            mediaController?.getTransportControls()?.play()
        }
        binding.pause.setOnClickListener {
            mediaController?.getTransportControls()?.pause()
        }

        lifecycleScope.launch {
            vm.actions.collect{
                when(it){
                    is BooksListVM.ActionAndState.showToast-> Toast.makeText(
                        requireContext(),
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return binding.root
    }

}