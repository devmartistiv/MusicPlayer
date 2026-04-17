package com.martist.musicplayer.data.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.martist.musicplayer.data.models.Album
import com.martist.musicplayer.data.models.Artist
@Entity
data class TrackDb (
    @PrimaryKey val id: Long,
//    val album_image: String,
    val duration: Long,
//    val link: String,
    val md5_image: String,
    var path: String,
//    val release_date: String,
    val title: String,
//    val title_short: String,
    val artist_name: String,
    var track_position: Int,
    var track_image : String,
    var artist_image : String
)
