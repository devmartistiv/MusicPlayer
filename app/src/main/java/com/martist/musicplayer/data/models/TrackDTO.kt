package com.martist.musicplayer.data.models

import com.martist.musicplayer.data.models.Artist

data class TrackDTO(
//    val album: Album,
    val duration: Long,
    val id: Long,
//    val link: String,
    val md5_image: String,
    val preview: String,
//    val release_date: String,
    val title: String,
//    val title_short: String,
    val artist: Artist,
    var track_position: Int
)