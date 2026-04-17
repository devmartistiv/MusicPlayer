package com.martist.musicplayer.data.repository

import android.content.Context
import android.util.Log
import com.martist.musicplayer.data.retrofit.ApiService
import com.martist.musicplayer.data.storage.mappers.TrackDTOToTrackDb
import com.martist.musicplayer.data.storage.dao.TrackDao
import com.martist.musicplayer.data.storage.entities.TrackDb
import com.martist.musicplayer.data.storage.mappers.TrackDbToTrackDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import javax.inject.Inject

class MusicRepository @Inject constructor(
    val apiService: ApiService,
    val trackDao: TrackDao,
    val trackDTOToTrackDb: TrackDTOToTrackDb,
    val trackDbToTrackDTO: TrackDbToTrackDTO,
) {

    suspend fun fetchTracks() = withContext(Dispatchers.IO) {
        apiService.getChart().data
    }

    suspend fun fetchTrackById(id: Long) = withContext(Dispatchers.IO) {
        apiService.getTrackById(id)
    }

    suspend fun getTracksInAlbum(albumId: Long) = withContext(Dispatchers.IO) {
        Log.d("test", albumId.toString())
        apiService.getTracksInAlbum(albumId)
    }

    suspend fun downloadTrack(context: Context?, id: Long) = withContext(Dispatchers.IO) {
        val track = apiService.getTrackById(id)
        val fileName = "${track.title}.mp3"
        var artistImage = "${track.artist.name}.jpg"
        val trackImage = "${track.title}.jpg"
        downloadPreview(context, track.preview, fileName)
        downloadPreview(context, track.artist.pictureURL, artistImage)
        downloadPreview(
            context,
            "https://e-cdns-images.dzcdn.net/images/cover/${track.md5_image}/250x250.jpg",
            trackImage
        )
        trackDao.insert(trackDTOToTrackDb(track).apply {
            path = fileName; artist_image = artistImage; track_image = trackImage
        })

    }

    fun downloadPreview(context: Context?, url: String, fileName: String) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) return

                val file = File(context?.filesDir, fileName)

                response.body?.byteStream().use { input ->
                    file.outputStream().use { output ->
                        input?.copyTo(output)
                    }
                }

            }
        })
    }

    suspend fun getDownloadedTracks() =
        trackDao.getAll()



    suspend fun getDownloadedTrack(id: Long) = withContext(Dispatchers.IO){
        trackDbToTrackDTO(trackDao.get(id))
    }



}