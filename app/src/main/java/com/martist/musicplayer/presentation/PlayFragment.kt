package com.martist.musicplayer.presentation

import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.martist.musicplayer.R
import com.martist.musicplayer.data.models.TrackDTO
import com.martist.musicplayer.databinding.FragmentPlayBinding
import com.martist.musicplayer.presentation.viewmodels.PlayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.io.FileSystem
import java.io.File

@AndroidEntryPoint
class PlayFragment : Fragment() {
    val viewModel: PlayViewModel by viewModels()
    private val args: PlayFragmentArgs by navArgs()
    private lateinit var binding: FragmentPlayBinding;
    private var controller: MediaController? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    fun initListeners(){
        val sessionToken = SessionToken(
            requireContext(),
            ComponentName(requireContext(), PlayerService::class.java)
        )
        val controllerBuilder =
            MediaController.Builder(requireContext(), sessionToken).buildAsync()
        controllerBuilder.addListener({
            controller = controllerBuilder.get()
            controller?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) startProgressUpdates()
                }

                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_READY) {
                        val duration = controller?.duration
                        binding.totalDuration.text = formatTime(duration ?: 0)
                    }
                }
            })

            if (args.isDownloaded) {
                viewModel.getDownloadedTrack(args.trackId)
            } else {
                viewModel.getTrack(args.trackId)
            }

            lifecycleScope.launch {
                viewModel.selectedTrack.collect {
                    if (context != null && it != null) {
                        val uri = if (args.isDownloaded) {
                            val file = File(context?.filesDir, "${it.title}.mp3")

                            Uri.fromFile(file).path

                        } else {
                            it.preview
                        }

                        val mediaItem = MediaItem.Builder().setUri(uri)
                            .setMediaMetadata(
                                MediaMetadata.Builder()

                                    .setTitle(it.title)
                                    .setArtist(it.artist.name)
                                    .build()
                            ).build()

                        controller?.setMediaItem(mediaItem)
                        controller?.prepare()

                    }
                    with(binding) {
                        if (it != null) {
                            val url = if (args.isDownloaded) {
                                File(context?.filesDir, "${it.title}.jpg")
                            } else {
                                "https://e-cdns-images.dzcdn.net/images/cover/${it.md5_image}/250x250.jpg"
                            }

                            trackImage.load(url) {
                                crossfade(true)
                                transformations(RoundedCornersTransformation(25f))
                                placeholder(R.drawable.rec_list_image)

                            }
                            trackName.text = it.title
                            trackAuthor.text = it.artist.name

                            val artistPicture = if (args.isDownloaded) {
                                File(context?.filesDir, "${it.artist.name}.jpg")
                            } else {
                                it.artist.pictureURL
                            }

                            authorImage.load(artistPicture) {
                                crossfade(true)
                                transformations(RoundedCornersTransformation(50f))
                                placeholder(R.drawable.rec_list_image)
                            }
                        }
                    }
                }
            }
        }, ContextCompat.getMainExecutor(requireContext()))


        binding.trackProgress.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    val duration = controller?.duration
                    val newPosition = (duration?.times(progress))?.div(100)
                    controller?.seekTo(newPosition ?: 0)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        }
        )

        binding.pause.setOnClickListener {
            binding.play.visibility = View.VISIBLE
            binding.pause.visibility = View.INVISIBLE
            controller?.pause()
        }
        binding.play.setOnClickListener {

            binding.pause.visibility = View.VISIBLE
            binding.play.visibility = View.INVISIBLE
            controller?.play()
        }
    }
    fun startProgressUpdates() {
        lifecycleScope.launch {
            while (controller?.isPlaying == true) {
                val position = controller?.currentPosition
                val duration = controller?.duration

                if (position != null && duration != null) {
                    if (duration > 0) {
                        val progress = (position.toFloat() / duration) * 100
                        binding.trackProgress.progress = progress.toInt()
                        binding.timePassed.text = formatTime(position)
                    }
                }
                delay(1000)
            }
        }
    }

    fun formatTime(mseconds: Long): String {
        val seconds = mseconds / 1000;
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", minutes, secs)
    }
}