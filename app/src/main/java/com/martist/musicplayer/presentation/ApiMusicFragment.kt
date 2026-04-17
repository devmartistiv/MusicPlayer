package com.martist.musicplayer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.martist.musicplayer.databinding.FragmentApiMuusicBinding
import com.martist.musicplayer.presentation.adapters.TrackListAdapter
import com.martist.musicplayer.presentation.viewmodels.ApiMusicViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ApiMusicFragment : Fragment() {

    val viewModel: ApiMusicViewModel by viewModels()
    lateinit var adapter: TrackListAdapter
    private lateinit var binding: FragmentApiMuusicBinding;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApiMuusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        initListeners()
    }

    fun initListeners() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText)
                return true
            }
        })

        binding.searchBar.setOnSearchClickListener {
            binding.forYouText.visibility = View.GONE
        }

        binding.searchBar.setOnCloseListener {
            binding.forYouText.visibility = View.VISIBLE
            return@setOnCloseListener false
        }
    }

    fun initList() {
        adapter = TrackListAdapter(
            isMyMusic = false,
            onClick = { id ->
                val action = ApiMusicFragmentDirections
                    .actionApiMusicFragmentToPlayFragment(id, false)
                findNavController().navigate(action)

            },
            onDownloadClick = { id ->
                lifecycleScope.launch {
                    viewModel.downloadTrack(activity, id)
                }
            }
        )
        binding.musicList.layoutManager = LinearLayoutManager(this.context)
        binding.musicList.adapter = adapter
        lifecycleScope.launch {
            viewModel.items.collect { items ->
                adapter.submitList(items)
            }
        }
    }
}