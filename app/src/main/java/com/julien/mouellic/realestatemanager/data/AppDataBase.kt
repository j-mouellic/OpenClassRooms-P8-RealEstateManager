package com.julien.mouellic.realestatemanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.julien.mouellic.realestatemanager.data.converter.BitmapConverter
import com.julien.mouellic.realestatemanager.data.converter.InstantConverter
import com.julien.mouellic.realestatemanager.data.converter.ListConverter
import com.julien.mouellic.realestatemanager.data.converter.LongListConverter
import com.julien.mouellic.realestatemanager.data.entity.*
import com.julien.mouellic.realestatemanager.data.dao.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import org.threeten.bp.Instant
import java.util.concurrent.Executors
import kotlin.random.Random

/**
 * Main Room Database for the RealEstateManager app.
 *
 * Responsibilities:
 * - Provides access to all DAOs.
 * - Manages entity classes and type converters.
 * - Initializes sample data when the database is first created.
 */
@Database(
    entities = [
        PropertyDTO::class,
        AgentDTO::class,
        LocationDTO::class,
        RealEstateTypeDTO::class,
        CommodityDTO::class,
        PictureDTO::class,
        PropertyCommodityCrossRefDTO::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    BitmapConverter::class,  // Convert Bitmap <-> ByteArray
    InstantConverter::class, // Convert Instant <-> Long
    ListConverter::class,    // Convert generic List <-> Comma-separated String
    LongListConverter::class // Convert List<Long> <-> Comma-separated String
)
abstract class AppDatabase : RoomDatabase() {

    // --- DAOs ---
    abstract fun propertyDao(): PropertyDAO
    abstract fun agentDao(): AgentDAO
    abstract fun locationDao(): LocationDAO
    abstract fun realEstateTypeDao(): RealEstateTypeDAO
    abstract fun commodityDao(): CommodityDAO
    abstract fun pictureDao(): PictureDAO
    abstract fun propertyWithDetailsDao(): PropertyWithDetailsDAO
    abstract fun propertyCommodityCrossRefDAO(): PropertyCommodityCrossRefDAO

    /**
     * Database callback for initialization when database is first created.
     */
    private class AppDatabaseCallback(
        private val scope: CoroutineScope,
        private val context: Context
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d(TAG, "Database onCreate called")
            INSTANCE?.let { database ->
                // Launch coroutine to populate the database
                scope.launch {
                    initDatabase(
                        context,
                        database.propertyDao(),
                        database.agentDao(),
                        database.commodityDao(),
                        database.realEstateTypeDao(),
                        database.locationDao(),
                        database.propertyCommodityCrossRefDAO(),
                        database.pictureDao()
                    )
                }
            }
        }

        /** ---------------- Helper Functions for Sample Data ---------------- **/

        private fun generateApartmentName(): String {
            val types = listOf("Apartment", "Loft", "Studio", "Penthouse")
            val floors = listOf("ground floor", "1st floor", "2nd floor", "top floor", "near Central Park")
            return "${types.random()} - ${floors.random()}"
        }

        private fun generateApartmentDescription(): String {
            val attributes = listOf(
                "spacious", "bright", "modern", "stylish", "cozy", "renovated",
                "hardwood floors", "large windows with natural light",
                "balcony with park views", "rooftop access",
                "open kitchen fully equipped", "luxury bathroom with walk-in shower",
                "steps away from Central Park", "city skyline view"
            )
            val selectedAttributes = attributes.shuffled().take(Random.nextInt(4, 7))
            return "Beautiful New York apartment, ${selectedAttributes.joinToString(", ")}. " +
                    "Perfect for enjoying the vibrant Manhattan lifestyle near Central Park."
        }

        private fun randomDaysBack(maxDays: Int = 365): Instant =
            Instant.now().minusSeconds(Random.nextLong(0, maxDays.toLong() * 24 * 3600))

        /** ---------------- Populate the database ---------------- **/
        private suspend fun initDatabase(
            context: Context,
            propertyDAO: PropertyDAO,
            agentDao: AgentDAO,
            commodityDAO: CommodityDAO,
            estateTypeDAO: RealEstateTypeDAO,
            locationDAO: LocationDAO,
            propertyCommodityDAO: PropertyCommodityCrossRefDAO,
            pictureDAO: PictureDAO
        ) {
            Log.d(TAG, "Initializing database with sample data")

            // --- Insert sample agents ---
            val idAgent1 = agentDao.insert(
                AgentDTO(1, "John", "Doe", "john.doe@email.com", "123456789", "Nestenn")
            )
            val idAgent2 = agentDao.insert(
                AgentDTO(2, "Jane", "Doe", "jane.doe@email.com", "987654321", "Nestenn")
            )

            // --- Insert sample real estate types ---
            val estateTypeNames = listOf(
                "Apartment", "House", "Loft", "Studio", "Villa", "Duplex", "Penthouse",
                "Chalet", "Farmhouse", "Townhouse", "Bungalow", "Manor"
            )
            estateTypeNames.forEachIndexed { index, name ->
                estateTypeDAO.insert(RealEstateTypeDTO(index + 1L, name))
            }

            // --- Insert sample commodities ---
            val commodityNames = listOf(
                "Shop", "Park", "School", "Hospital", "Metro Station", "Supermarket",
                "Gym", "Pharmacy", "Playground", "Bakery", "Cinema", "Bus Stop"
            )
            commodityNames.forEachIndexed { index, name ->
                commodityDAO.insert(CommodityDTO(index + 1L, name))
            }

            // --- Insert sample properties with locations, commodities, and pictures ---
            val centerLat = 40.766
            val centerLng = -73.9832222223
            fun randomOffset() = Random.nextDouble(-0.002, 0.002)

            val propertyAddresses = listOf(
                301 to "W 56th St", 325 to "W 56th St", 251 to "W 55th St", 271 to "W 55th St",
                334 to "W 57th St", 380 to "W 57th St", 251 to "W 58th St", 299 to "W 58th St",
                842 to "9th Ave", 862 to "9th Ave", 968 to "8th Ave", 974 to "8th Ave"
            )

            val locationIds = mutableListOf<Long>()
            for (i in 1..10) {
                val (num, streetName) = propertyAddresses[i - 1]
                val locationId = locationDAO.insert(
                    LocationDTO(
                        id = null,
                        latitude = centerLat + randomOffset(),
                        longitude = centerLng + randomOffset(),
                        street = streetName,
                        streetNumber = num,
                        city = "New York",
                        postalCode = "10019",
                        country = "United States"
                    )
                )
                locationIds.add(locationId)
            }

            // --- Create 10 unique properties ---
            for (i in 1..10) {
                val propertyId = propertyDAO.insert(
                    PropertyDTO(
                        name = generateApartmentName(),
                        description = generateApartmentDescription(),
                        surface = Random.nextInt(80, 200).toDouble(),
                        numbersOfRooms = Random.nextInt(4, 10),
                        numbersOfBathrooms = Random.nextInt(1, 3),
                        numbersOfBedrooms = Random.nextInt(1, 4),
                        price = Random.nextInt(1_500_000, 6_000_000).toDouble(),
                        isSold = Random.nextBoolean(),
                        creationDate = randomDaysBack(),
                        entryDate = randomDaysBack(30),
                        saleDate = if (Random.nextBoolean()) randomDaysBack(60) else null,
                        apartmentNumber = Random.nextInt(1, 30),
                        realEstateTypeId = 1L,
                        locationId = locationIds[i - 1],
                        agentId = if (i % 2 == 0) 1L else 2L
                    )
                )

                // --- Insert random commodities for the property ---
                (1..12).shuffled().take(Random.nextInt(2, 6)).forEach { cId ->
                    propertyCommodityDAO.insert(PropertyCommodityCrossRefDTO(propertyId, cId.toLong()))
                }

                // --- Insert pictures ---
                insertPictures(context, pictureDAO, propertyId, i)
            }

            // --- Create 2 additional properties in the same building ---
            val sharedLocationId = locationIds.random() // pick one of the 10 existing locations
            for (i in 11..12) {
                val propertyId = propertyDAO.insert(
                    PropertyDTO(
                        name = generateApartmentName(),
                        description = generateApartmentDescription(),
                        surface = Random.nextInt(80, 200).toDouble(),
                        numbersOfRooms = Random.nextInt(4, 10),
                        numbersOfBathrooms = Random.nextInt(1, 3),
                        numbersOfBedrooms = Random.nextInt(1, 4),
                        price = Random.nextInt(1_500_000, 6_000_000).toDouble(),
                        isSold = Random.nextBoolean(),
                        creationDate = randomDaysBack(),
                        entryDate = randomDaysBack(30),
                        saleDate = if (Random.nextBoolean()) randomDaysBack(60) else null,
                        apartmentNumber = Random.nextInt(31, 60),
                        realEstateTypeId = 1L,
                        locationId = sharedLocationId,
                        agentId = if (i % 2 == 0) 1L else 2L
                    )
                )

                (1..12).shuffled().take(Random.nextInt(2, 6)).forEach { cId ->
                    propertyCommodityDAO.insert(PropertyCommodityCrossRefDTO(propertyId, cId.toLong()))
                }

                insertPictures(context, pictureDAO, propertyId, i)
            }
        }

        private suspend fun insertPictures(
            context: Context,
            pictureDAO: PictureDAO,
            propertyId: Long,
            index: Int
        ) {
            val converter = BitmapConverter()
            for (order in 1..5) {
                val resourceName = "flat_${((index - 1) % 6) + 1}_${order}"
                val resId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
                if (resId != 0) {
                    val bitmap = BitmapFactory.decodeResource(context.resources, resId)
                    val content = converter.fromBitmap(bitmap)
                    val thumbnailContent = converter.fromBitmap(bitmap)
                    pictureDAO.insert(
                        PictureDTO(
                            id = null,
                            content = content ?: ByteArray(0),
                            thumbnailContent = thumbnailContent ?: ByteArray(0),
                            order = order - 1,
                            propertyId = propertyId
                        )
                    )
                } else {
                    Log.w("DBInit", "Image resource $resourceName not found!")
                }
            }
        }
    }



    /** ---------------- Singleton Pattern ---------------- **/
    companion object {
        private const val TAG = "AppDatabase"
        private const val DATABASE_NAME = "RealEstateManagerDB"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns the singleton instance of the database.
         * Initializes it if it does not exist.
         */
        fun getDatabase(context: Context, coroutineScope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(AppDatabaseCallback(coroutineScope, context))
                    .setQueryCallback(
                        QueryCallback { sqlQuery, bindArgs ->
                            Log.d("ROOM_SQL", "Query: $sqlQuery | Args: $bindArgs")
                        },
                        Executors.newSingleThreadExecutor()
                    )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
