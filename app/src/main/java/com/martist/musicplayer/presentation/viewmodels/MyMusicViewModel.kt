package com.martist.musicplayer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martist.musicplayer.data.models.TrackDTO
import com.martist.musicplayer.data.repository.MusicRepository
import com.martist.musicplayer.data.storage.mappers.TrackDbToTrackDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyMusicViewModel @Inject constructor(
    val repository: MusicRepository,
    val trackDbToTrackDTO: TrackDbToTrackDTO
) : ViewModel() {

    val items: MutableStateFlow<List<TrackDTO>> = MutableStateFlow(emptyList())


    init {
        loadItems()
    }

    fun loadItems() =
        viewModelScope.launch {
             repository.getDownloadedTracks().collect { list -> items.value = list.map { trackDbToTrackDTO(it) } }
        }
}