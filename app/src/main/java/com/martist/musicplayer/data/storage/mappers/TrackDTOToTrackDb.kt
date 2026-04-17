package com.martist.musicplayer.data.storage.mappers

import com.martist.musicplayer.data.models.TrackDTO
import com.martist.musicplayer.data.storage.entities.TrackDb
import jakarta.inject.Inject

class TrackDTOToTrackDb @Inject constructor() : (TrackDTO)-> TrackDb {
    override fun invoke(trackDTO: TrackDTO): TrackDb {
        return TrackDb(
            id = trackDTO.id,
            duration = trackDTO.duration,
            md5_image = trackDTO.md5_image,
            title = trackDTO.title,
            artist_name = trackDTO.artist.name,
            track_position = trackDTO.track_position,
            path = "",
            track_image = "",
            artist_image = ""

        )
    }
}