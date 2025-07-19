package com.julien.mouellic.realestatemanager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.julien.mouellic.realestatemanager.data.entity.LocationDTO

@Dao
interface LocationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationDTO): Long

    @Update
    suspend fun update(location: LocationDTO)

    @Delete
    suspend fun delete(location: LocationDTO)

    @Query("SELECT * FROM locations")
    suspend fun getAllLocations(): List<LocationDTO>

    @Query("SELECT * FROM locations WHERE location_id = :id")
    suspend fun getLocationById(id: Long): LocationDTO?
}
