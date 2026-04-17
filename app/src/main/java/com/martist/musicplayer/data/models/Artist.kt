package com.martist.musicplayer.data.models

import com.google.gson.annotations.SerializedName

data class Artist(
    var id: Long,
    var name: String,
    @SerializedName("picture")
    var pictureURL: String,
)