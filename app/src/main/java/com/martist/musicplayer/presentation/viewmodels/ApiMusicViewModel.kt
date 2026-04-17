package com.martist.musicplayer.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martist.musicplayer.data.models.TrackDTO
import com.martist.musicplayer.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiMusicViewModel @Inject constructor(
    val apiMusicRepository: MusicRepository
) : ViewModel() {

    val items: MutableStateFlow<List<TrackDTO>> = MutableStateFlow(emptyList())

    init {
        loadItems()
    }

    fun loadItems() =
        viewModelScope.launch {
            items.value = apiMusicRepository.fetchTracks()
        }

    fun downloadTrack(context: Context?, id: Long) =
        viewModelScope.launch {
            apiMusicRepository.downloadTrack(context, id)
        }
}