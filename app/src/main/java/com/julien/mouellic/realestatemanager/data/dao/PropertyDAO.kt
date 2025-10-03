package com.julien.mouellic.realestatemanager.data.dao

import androidx.room.*
import com.julien.mouellic.realestatemanager.data.entity.PropertyDTO
import com.julien.mouellic.realestatemanager.data.flatten.PropertyListItemFlatten
import kotlinx.coroutines.flow.Flow

/**
 * DAO for the Property table.
 * Handles insertion, update, deletion, and querying of properties.
 *
 * - `insert` / `insertAll`: add one or more properties.
 * - `update` / `updateSoldStatus`: modify property fields or sold status.
 * - `delete` / `deleteAll`: remove one or more properties.
 * - `getByIdRT` / `getAllRT` / `getAllNewerToOlderRT`: provide real-time Flow updates.
 * - `getById` / `getAll` / `getAllNewerToOlder`: suspend functions for fetching data once.
 *
 * The `search` query:
 * - Returns a list of `PropertyListItemFlatten`.
 * - `PropertyListItemFlatten` is a lighter version of `PropertyDTO`, optimized for listing queries.
 * - It includes main property info, type, location, agent name, first picture, and aggregated commodities.
 * - Supports filtering by type, price, surface, number of rooms, and availability.
 */
@Dao
interface PropertyDAO {

    /** INSERT **/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(property: PropertyDTO): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(properties: List<PropertyDTO>): List<Long>

    /** UPDATE **/
    @Update
    suspend fun update(property: PropertyDTO): Int

    @Update
    suspend fun update(properties: List<PropertyDTO>): Int

    @Query("UPDATE properties SET is_sold = :sold WHERE id = :id")
    suspend fun updateSoldStatus(sold: Boolean, id: Long)

    /** DELETE **/
    @Query("DELETE FROM properties")
    suspend fun deleteAll()

    @Query("DELETE FROM properties WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Delete
    suspend fun delete(property: PropertyDTO)

    @Delete
    suspend fun delete(properties: List<PropertyDTO>)

    /** SELECT REAL TIME / FLOW **/
    @Query("SELECT * FROM properties WHERE id = :id")
    fun getByIdRT(id: Long): Flow<PropertyDTO?>

    @Query("SELECT * FROM properties")
    fun getAllRT(): Flow<List<PropertyDTO>>

    @Query("SELECT * FROM properties ORDER BY id DESC")
    fun getAllNewerToOlderRT(): Flow<List<PropertyDTO>>

    /** SELECT SUSPENDED **/

    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getById(id: Long): PropertyDTO?

    @Query("SELECT * FROM properties")
    suspend fun getAll(): List<PropertyDTO>

    @Query("SELECT * FROM properties ORDER BY id DESC")
    suspend fun getAllNewerToOlder(): List<PropertyDTO>

    @Query(
        """
    SELECT 
        -- SELECT & AS: choose which columns to retrieve and rename them to match DTO/Kotlin fields
        p.id AS id,
        p.name AS name,
        p.description AS description,
        p.surface AS surface,
        p.numbers_of_rooms AS nbRooms,
        p.price AS price,
        p.is_sold AS isSold,
        p.creation_date AS dateCreation,
        p.entry_date AS entryDate,
        p.sale_date AS saleDate,

        -- LEFT JOIN with real_estate_types: include property even if type is missing
        t.name AS type,

        -- LEFT JOIN with locations: include property even if location is missing
        l.street AS street,
        l.postal_code AS postalCode,
        l.city AS city,
        l.country AS country,
        l.longitude AS longitude,
        l.latitude AS latitude,

        -- LEFT JOIN with agents: include property even if agent is missing
        a.first_name || ' ' || a.last_name AS agentName,  

        -- GROUP_CONCAT + COALESCE: combine multiple commodity names into a single comma-separated string
        -- COALESCE ensures an empty string if no commodities exist
        COALESCE(GROUP_CONCAT(c.name, ','), '') AS commoditiesType,    

        -- GROUP_CONCAT + COALESCE: combine multiple commodity IDs into a single comma-separated string
        COALESCE(GROUP_CONCAT(c.id, ','), '') AS commoditiesIds,   

        -- LEFT JOIN with pictures: include first picture (order = 0), NULL if none
        pi.content AS picture  

    FROM properties p

    -- LEFT JOINs: ensure that all properties appear even if related tables are missing
    LEFT JOIN real_estate_types t ON t.id = p.real_estate_type_id
    LEFT JOIN locations l ON l.id = p.location_id
    LEFT JOIN agents a ON a.id = p.agent_id
    LEFT JOIN property_commodity pc ON pc.property_id = p.id  
    LEFT JOIN commodities c ON c.id = pc.commodity_id
    LEFT JOIN pictures pi ON pi.property_id = p.id AND pi.`order` = 0  

    -- WHERE: dynamic filters; if parameter is NULL, filter is ignored
    WHERE
        (:type IS NULL OR t.id = :type) AND
        (:minPrice IS NULL OR p.price >= :minPrice) AND
        (:maxPrice IS NULL OR p.price <= :maxPrice) AND
        (:minSurface IS NULL OR p.surface >= :minSurface) AND
        (:maxSurface IS NULL OR p.surface <= :maxSurface) AND
        (:minNbRooms IS NULL OR p.numbers_of_rooms >= :minNbRooms) AND
        (:maxNbRooms IS NULL OR p.numbers_of_rooms <= :maxNbRooms) AND
        (:isAvailable IS NULL OR p.is_sold = :isAvailable)

    -- GROUP BY: collapse multiple joined rows (commodities, pictures) into a single row per property
    GROUP BY p.id
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
    ): List<PropertyListItemFlatten>


}
