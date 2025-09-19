package com.julien.mouellic.realestatemanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.julien.mouellic.realestatemanager.data.converter.BitmapConverter
import com.julien.mouellic.realestatemanager.data.converter.InstantConverter
import com.julien.mouellic.realestatemanager.data.converter.ListConverter
import com.julien.mouellic.realestatemanager.data.entity.*
import com.julien.mouellic.realestatemanager.data.dao.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.julien.mouellic.realestatemanager.data.converter.LongListConverter
import org.threeten.bp.Instant
import kotlin.random.Random


@Database(
    entities = [
        PropertyDTO::class,
        AgentDTO::class,
        LocationDTO::class,
        RealEstateTypeDTO::class,
        CommodityDTO::class,
        PictureDTO::class,
        PropertyCommodityCrossRefDTO::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    BitmapConverter::class,
    InstantConverter::class,
    ListConverter::class,
    LongListConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDAO
    abstract fun agentDao(): AgentDAO
    abstract fun locationDao(): LocationDAO
    abstract fun realEstateTypeDao(): RealEstateTypeDAO
    abstract fun commodityDao(): CommodityDAO
    abstract fun pictureDao(): PictureDAO
    abstract fun propertyWithDetailsDao(): PropertyWithDetailsDAO
    abstract fun propertyCommodityCrossRefDAO(): PropertyCommodityCrossRefDAO

    private class AppDatabaseCallback(
        private val scope: CoroutineScope,
        private val context: Context
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d(TAG, "onCreate called")
            INSTANCE?.let { database ->
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

        // Helper functions
        fun randomOffset(): Double = Random.nextDouble(-0.0045, 0.0045) // ≈500m
        fun randomStreetNumber(): Int = Random.nextInt(1, 100)
        fun randomStreetName(): String {
            val streets = listOf("Rivoli", "Saint-Honoré", "Montmartre", "Saint-Germain", "Voltaire", "Bastille", "Louvre", "Tuileries", "Opéra", "Lafayette")
            return streets.random()
        }

        fun generateApartmentName(): String {
            val types = listOf("Apartment", "Loft", "Studio", "Penthouse")
            val floors = listOf("ground floor", "1st floor", "2nd floor", "top floor", "near Central Park")


            return "${types.random()} - ${floors.random()}"
        }

        fun generateApartmentDescription(): String {
            val attributes = listOf(
                "spacious", "bright", "modern", "stylish", "cozy", "renovated",
                "hardwood floors", "large windows with natural light",
                "balcony with park views", "rooftop access",
                "open kitchen fully equipped", "luxury bathroom with walk-in shower",
                "steps away from Central Park", "city skyline view"
            )


            val selectedAttributes = attributes.shuffled().take(Random.nextInt(4, 7))


            return "Beautiful New York apartment, " +
                    selectedAttributes.joinToString(", ") +
                    ". Perfect for enjoying the vibrant Manhattan lifestyle near Central Park."
        }
        fun randomDaysBack(maxDays: Int = 365): Instant = Instant.now().minusSeconds(Random.nextLong(0, maxDays.toLong() * 24 * 3600))


        // Populate database
        suspend fun initDatabase(
            context: Context,
            propertyDAO: PropertyDAO,
            agentDao : AgentDAO,
            commodityDAO: CommodityDAO,
            estateTypeDAO: RealEstateTypeDAO,
            locationDAO: LocationDAO,
            propertyCommodityDAO: PropertyCommodityCrossRefDAO,
            pictureDAO: PictureDAO
        ) {

            Log.d(TAG, "initializing Database")

            // Agents
            val idAgent1 = agentDao.insert(
                AgentDTO(id = 1, firstName = "John", lastName = "Doe", email = "john.doe@email.com", phoneNumber = "123456789", realEstateAgency = "Nestenn")
            )

            Log.d(TAG, "Agent 1 inserted with id $idAgent1")

            val idAgent2 = agentDao.insert(
                AgentDTO(id = 2, firstName = "Jane", lastName = "Doe", email = "jane.doe@email.com", phoneNumber = "987654321", realEstateAgency = "Nestenn")
            )

            Log.d(TAG, "Agent 2 inserted with id $idAgent2")

            // Estate types
            val idEstateType1 = estateTypeDAO.insert(
                RealEstateTypeDTO(id = 1, name = "Apartment")
            )

            Log.d(TAG, "EstateType 1 inserted with id $idEstateType1")

            val idEstateType2 = estateTypeDAO.insert(
                RealEstateTypeDTO(id = 2, name = "House")
            )

            Log.d(TAG, "EstateType 2 inserted with id $idEstateType2")

            val idEstateType3 = estateTypeDAO.insert(RealEstateTypeDTO(id = 3, name = "Loft"))
            Log.d(TAG, "EstateType 3 inserted with id $idEstateType3")

            val idEstateType4 = estateTypeDAO.insert(RealEstateTypeDTO(id = 4, name = "Studio"))
            Log.d(TAG, "EstateType 4 inserted with id $idEstateType4")

            val idEstateType5 = estateTypeDAO.insert(RealEstateTypeDTO(id = 5, name = "Villa"))
            Log.d(TAG, "EstateType 5 inserted with id $idEstateType5")

            val idEstateType6 = estateTypeDAO.insert(RealEstateTypeDTO(id = 6, name = "Duplex"))
            Log.d(TAG, "EstateType 6 inserted with id $idEstateType6")

            val idEstateType7 = estateTypeDAO.insert(RealEstateTypeDTO(id = 7, name = "Penthouse"))
            Log.d(TAG, "EstateType 7 inserted with id $idEstateType7")

            val idEstateType8 = estateTypeDAO.insert(RealEstateTypeDTO(id = 8, name = "Chalet"))
            Log.d(TAG, "EstateType 8 inserted with id $idEstateType8")

            val idEstateType9 = estateTypeDAO.insert(RealEstateTypeDTO(id = 9, name = "Farmhouse"))
            Log.d(TAG, "EstateType 9 inserted with id $idEstateType9")

            val idEstateType10 = estateTypeDAO.insert(RealEstateTypeDTO(id = 10, name = "Townhouse"))
            Log.d(TAG, "EstateType 10 inserted with id $idEstateType10")

            val idEstateType11 = estateTypeDAO.insert(RealEstateTypeDTO(id = 11, name = "Bungalow"))
            Log.d(TAG, "EstateType 11 inserted with id $idEstateType11")

            val idEstateType12 = estateTypeDAO.insert(RealEstateTypeDTO(id = 12, name = "Manor"))
            Log.d(TAG, "EstateType 12 inserted with id $idEstateType12")

            // Commodities
            val idCommodity1 = commodityDAO.insert(
                CommodityDTO(id = 1, name = "Shop")
            )
            Log.d(TAG, "Commodity 1 inserted with id $idCommodity1")

            val idCommodity2 = commodityDAO.insert(
                CommodityDTO(id = 2, name = "Park")
            )
            Log.d(TAG, "Commodity 2 inserted with id $idCommodity2")

            val idCommodity3 = commodityDAO.insert(CommodityDTO(id = 3, name = "School"))
            Log.d(TAG, "Commodity 3 inserted with id $idCommodity3")

            val idCommodity4 = commodityDAO.insert(CommodityDTO(id = 4, name = "Hospital"))
            Log.d(TAG, "Commodity 4 inserted with id $idCommodity4")

            val idCommodity5 = commodityDAO.insert(CommodityDTO(id = 5, name = "Metro Station"))
            Log.d(TAG, "Commodity 5 inserted with id $idCommodity5")

            val idCommodity6 = commodityDAO.insert(CommodityDTO(id = 6, name = "Supermarket"))
            Log.d(TAG, "Commodity 6 inserted with id $idCommodity6")

            val idCommodity7 = commodityDAO.insert(CommodityDTO(id = 7, name = "Gym"))
            Log.d(TAG, "Commodity 7 inserted with id $idCommodity7")

            val idCommodity8 = commodityDAO.insert(CommodityDTO(id = 8, name = "Pharmacy"))
            Log.d(TAG, "Commodity 8 inserted with id $idCommodity8")

            val idCommodity9 = commodityDAO.insert(CommodityDTO(id = 9, name = "Playground"))
            Log.d(TAG, "Commodity 9 inserted with id $idCommodity9")

            val idCommodity10 = commodityDAO.insert(CommodityDTO(id = 10, name = "Bakery"))
            Log.d(TAG, "Commodity 10 inserted with id $idCommodity10")

            val idCommodity11 = commodityDAO.insert(CommodityDTO(id = 11, name = "Cinema"))
            Log.d(TAG, "Commodity 11 inserted with id $idCommodity11")

            val idCommodity12 = commodityDAO.insert(CommodityDTO(id = 12, name = "Bus Stop"))
            Log.d(TAG, "Commodity 12 inserted with id $idCommodity12")

            // Properties
            val centerLat = 40.7660000000
            val centerLng = -73.9832222223

            fun randomOffset(): Double = Random.nextDouble(from = -0.002, until = 0.002)

            val propertiesAddresses = listOf(
                Pair(301, "W 56th St"),
                Pair(325, "W 56th St"),
                Pair(251, "W 55th St"),
                Pair(271, "W 55th St"),
                Pair(334, "W 57th St"),
                Pair(380, "W 57th St"),
                Pair(251, "W 58th St"),
                Pair(299, "W 58th St"),
                Pair(842, "9th Ave"),
                Pair(862, "9th Ave"),
                Pair(968, "8th Ave"),
                Pair(974, "8th Ave")
            )

            for (i in 1..12) {
                Log.d(TAG, "Create property : $i")

                // Location
                val (num, streetName) = propertiesAddresses[i - 1]
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

                // Property
                val propertyName = generateApartmentName()
                val propertyDescription = generateApartmentDescription()
                val typeId = 1L // RealEstateType id
                val agentId = if (i % 2 == 0) 1L else 2L
                val surface = Random.nextInt(80, 200).toDouble()
                val pricePerM2 = Random.nextInt(15000, 30000)
                val price = surface * pricePerM2
                val rooms = Random.nextInt(4, 10)
                val bathrooms = Random.nextInt(1, 3)
                val bedrooms = if (rooms > 1) Random.nextInt(1, rooms) else 1
                val creationDate = randomDaysBack(365)
                val isSold = Random.nextBoolean()
                val entryDate = creationDate.plusSeconds(Random.nextLong(1, 30) * 24 * 3600)
                val saleDate = if (isSold) entryDate.plusSeconds(Random.nextLong(1, 60) * 24 * 3600) else null

                val propertyId = propertyDAO.insert(
                    PropertyDTO(
                        name = propertyName,
                        description = propertyDescription,
                        surface = surface,
                        numbersOfRooms = rooms,
                        numbersOfBathrooms = bathrooms,
                        numbersOfBedrooms = bedrooms,
                        price = price,
                        isSold = isSold,
                        creationDate = creationDate,
                        entryDate = entryDate,
                        saleDate = saleDate,
                        apartmentNumber = Random.nextInt(1, 30),
                        realEstateTypeId = typeId,
                        locationId = locationId,
                        agentId = agentId
                    )
                )

                // Commodities (2-5 random)
                val commodityIds = (1..12).shuffled().take(Random.nextInt(2, 6))
                commodityIds.forEach { cId ->
                    propertyCommodityDAO.insert(PropertyCommodityCrossRefDTO(propertyId = propertyId, commodityId = cId.toLong()))
                }

                // Pictures (5 per property)
                val converter = BitmapConverter()
                val flatIndex = if (i <= 6) i else (i - 6)

                for (order in 1..5) {
                    val resourceName = "flat_${flatIndex}_${order}"
                    val resId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)

                    if (resId != 0) {
                        val bitmap = BitmapFactory.decodeResource(context.resources, resId)
                        val content = converter.fromBitmap(bitmap)
                        val thumbnailContent = converter.fromBitmap(bitmap)

                        val pictureDTO = PictureDTO(
                            id = null,
                            content = content ?: ByteArray(0),
                            thumbnailContent = thumbnailContent ?: ByteArray(0),
                            order = order - 1,
                            propertyId = propertyId
                        )

                        pictureDAO.insert(pictureDTO)
                    } else {
                        Log.w("DBInit", "Image resource $resourceName not found !")
                    }
                }

            }

        }
    }



    companion object {
        private const val TAG = "AppDatabase"
        private const val DATABASE_NAME = "RealEstateManagerDB"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, coroutineScope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(AppDatabaseCallback(coroutineScope, context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}