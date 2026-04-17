package com.martist.musicplayer.data.storage.mappers

import com.martist.musicplayer.data.models.Album
import com.martist.musicplayer.data.models.TrackDTO
import com.martist.musicplayer.data.storage.entities.TrackDb
import com.martist.musicplayer.data.models.Artist
import jakarta.inject.Inject

class TrackDbToTrackDTO @Inject constructor() : (TrackDb)-> TrackDTO {
    override fun invoke(TrackDb: TrackDb): TrackDTO {
        return TrackDTO(
            id = TrackDb.id,
            duration = TrackDb.duration,
            md5_image = TrackDb.md5_image,
            title = TrackDb.title,
            artist = Artist(id = 0,   TrackDb.artist_name,TrackDb.track_image),
            track_position = TrackDb.track_position,
            preview = TrackDb.path,
        )
    }
}