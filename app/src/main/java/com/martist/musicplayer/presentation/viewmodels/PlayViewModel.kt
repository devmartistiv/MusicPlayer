package com.martist.musicplayer.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martist.musicplayer.data.models.TrackDTO
import com.martist.musicplayer.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PlayViewModel @Inject constructor(
    val apiMusicRepository: MusicRepository,
) : ViewModel() {

    var selectedTrack = MutableStateFlow<TrackDTO?>(null)

    fun getTrack(id: Long) =
        viewModelScope.launch {
            selectedTrack.value =
                apiMusicRepository.fetchTrackById(id);
        }

    fun getDownloadedTrack(id: Long) =
        viewModelScope.launch {
            selectedTrack.value =
                apiMusicRepository.getDownloadedTrack(id);
        }
}