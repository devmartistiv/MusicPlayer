package com.martist.musicplayer.di

import com.martist.musicplayer.data.repository.MusicRepository
import com.martist.musicplayer.data.retrofit.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object ApplicationModule {

    @Provides
    fun provideRetrofit(): Retrofit {

        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    val baseUrl = "https://api.deezer.com/"
}