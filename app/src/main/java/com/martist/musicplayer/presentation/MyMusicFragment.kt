package com.martist.musicplayer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.martist.musicplayer.databinding.FragmentMyMusicBinding
import com.martist.musicplayer.presentation.adapters.TrackListAdapter
import com.martist.musicplayer.presentation.viewmodels.MyMusicViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyMusicFragment : Fragment() {

    var binding: FragmentMyMusicBinding? = null
    val viewModel: MyMusicViewModel by viewModels()
    lateinit var adapter: TrackListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyMusicBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onDetach() {
        super.onDetach()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrackListAdapter(
            isMyMusic = true,
            onClick = { id ->
                val action = MyMusicFragmentDirections.actionMyMusicFragmentToPlayFragment(id, true)
                findNavController().navigate(action)

            },
            onDownloadClick = { id ->

            }
        )

        binding?.let {
            it.musicList.layoutManager = LinearLayoutManager(this.context)
            it.musicList.adapter = adapter
        }

        lifecycleScope.launch {
            viewModel.items.collect { items ->
                adapter.submitList(items)
            }
        }
    }
}