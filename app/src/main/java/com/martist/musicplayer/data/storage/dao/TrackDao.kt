package com.martist.musicplayer.data.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.martist.musicplayer.data.storage.entities.TrackDb
import kotlinx.coroutines.flow.Flow
@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trackDb: TrackDb)
    @Query("SELECT * FROM TrackDb WHERE id=:id")
    suspend fun  get(id: Long): TrackDb
    @Query("SELECT * FROM TrackDb")
     fun  getAll(): Flow<List<TrackDb>>
}