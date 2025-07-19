package com.julien.mouellic.realestatemanager.data.dao

import androidx.room.*
import com.julien.mouellic.realestatemanager.data.entity.PropertyDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDAO {

    /** INSERT **/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(property: PropertyDTO): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(properties: List<PropertyDTO>): List<Long>

    /** UPDATE **/
    @Update
    suspend fun update(property: PropertyDTO): Int

    @Update
    suspend fun update(properties: List<PropertyDTO>): Int

    @Query("UPDATE properties SET is_sold = :sold WHERE property_id = :id")
    suspend fun updateSoldStatus(sold: Boolean, id: Long)

    /** DELETE **/
    @Query("DELETE FROM properties")
    suspend fun deleteAll()

    @Query("DELETE FROM properties WHERE property_id = :id")
    suspend fun deleteById(id: Long)

    @Delete
    suspend fun delete(property: PropertyDTO)

    @Delete
    suspend fun delete(properties: List<PropertyDTO>)

    /** SELECT REAL TIME / FLOW **/
    @Query("SELECT * FROM properties WHERE property_id = :id")
    fun getByIdRT(id: Long): Flow<PropertyDTO?>

    @Query("SELECT * FROM properties")
    fun getAllRT(): Flow<List<PropertyDTO>>

    @Query("SELECT * FROM properties ORDER BY property_id DESC")
    fun getAllNewerToOlderRT(): Flow<List<PropertyDTO>>

    /** SELECT SUSPENDED **/
    @Query("SELECT * FROM properties WHERE property_id = :id")
    suspend fun getById(id: Long): PropertyDTO?

    @Query("SELECT * FROM properties")
    suspend fun getAll(): List<PropertyDTO>

    @Query("SELECT * FROM properties ORDER BY property_id DESC")
    suspend fun getAllNewerToOlder(): List<PropertyDTO>

    @Query(
        """
        SELECT 
            p.property_id AS id,
            p.name,
            p.description,
            p.surface AS surface,
            p.numbers_of_rooms AS numbersOfRooms,
            p.price,
            p.is_sold,
            p.creation_date AS creationDate,
            p.entry_date AS entryDate,
            p.sale_date AS saleDate,
            t.name AS typeName,
            l.street,
            l.postal_code AS postalCode,
            l.city,
            l.country,
            l.longitude,
            l.latitude,
            a.first_name || ' ' || a.last_name AS agentName,
            COALESCE(GROUP_CONCAT(c.name,','),'') AS commoditiesName,
            pi.content AS picture
        FROM properties p
        LEFT JOIN real_estate_types t ON t.real_estate_type_id = p.real_estate_type_id
        LEFT JOIN locations l ON l.location_id = p.location_id
        LEFT JOIN agents a ON a.agent_id = p.agent_id
        LEFT JOIN property_commodity pc ON pc.property_id = p.property_id 
        LEFT JOIN commodities c ON c.commodity_id = pc.commodity_id
        LEFT JOIN pictures pi ON pi.property_id = p.property_id AND pi.`order` = 0
        WHERE
            (:type IS NULL OR t.real_estate_type_id = :type) AND
            (:minPrice IS NULL OR p.price >= :minPrice) AND
            (:maxPrice IS NULL OR p.price <= :maxPrice) AND
            (:minSurface IS NULL OR p.surface >= :minSurface) AND
            (:maxSurface IS NULL OR p.surface <= :maxSurface) AND
            (:minNbRooms IS NULL OR p.numbers_of_rooms >= :minNbRooms) AND
            (:maxNbRooms IS NULL OR p.numbers_of_rooms <= :maxNbRooms) AND
            (:isAvailable IS NULL OR p.is_sold = :isAvailable)
        GROUP BY p.property_id
        """
    )
    suspend fun search(
        type: Long?,
        minPrice: Double?,
        maxPrice: Double?,
        minSurface: Double?,
        maxSurface: Double?,
        minNbRooms: Int?,
        maxNbRooms: Int?,
        isAvailable: Boolean?,
    ): List<PropertyDTO>

}
