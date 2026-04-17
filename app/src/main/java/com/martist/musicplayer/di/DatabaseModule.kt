package com.martist.musicplayer.di

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.martist.musicplayer.data.storage.AppDatabase
import com.martist.musicplayer.data.storage.dao.TrackDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provide(@ApplicationContext context: Context) =
        databaseBuilder(
            context,
            AppDatabase::class.java, "main"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    @Singleton
    fun provideTracksDao(db: AppDatabase): TrackDao = db.trackDao()
}