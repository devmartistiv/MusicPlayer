package com.martist.musicplayer.data.retrofit

import com.martist.musicplayer.data.models.TrackDTO
import com.martist.musicplayer.data.models.TracksDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("chart/0/tracks")
    suspend fun getChart(): TracksDTO

    @GET("track/{id}")
    suspend fun getTrackById(@Path("id") id:Long): TrackDTO
    @GET("https://api.deezer.com/album/{albumId}/tracks}")
    suspend fun getTracksInAlbum(@Path("albumId") id:Long): TracksDTO
}