package com.martist.musicplayer.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.martist.musicplayer.data.storage.dao.TrackDao
import com.martist.musicplayer.data.storage.entities.TrackDb

@Database(entities = [TrackDb::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}