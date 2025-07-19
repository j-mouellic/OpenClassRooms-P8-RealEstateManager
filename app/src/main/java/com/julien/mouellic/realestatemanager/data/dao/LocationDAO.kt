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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(location: LocationDTO): Long

    @Update
    suspend fun update(location: LocationDTO): Int

    @Delete
    suspend fun delete(location: LocationDTO)

    @Query("DELETE FROM locations WHERE location_id NOT IN (SELECT location_id FROM properties)")
    suspend fun deleteUnused()

    @Query("SELECT * FROM locations")
    suspend fun getAllLocations(): List<LocationDTO>

    @Query("SELECT * FROM locations WHERE location_id = :id")
    suspend fun getLocationById(id: Long): LocationDTO?

    @Query("SELECT location_id FROM locations WHERE street = :street AND (street_number IS NULL OR street_number = :number) AND postal_code = :postalCode AND city = :city AND country = :country")
    suspend fun search(street: String, number: Int?, postalCode: String, city: String, country: String): Long?
}
