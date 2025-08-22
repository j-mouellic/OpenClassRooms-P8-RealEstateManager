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
    ListConverter::class
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
            val types = listOf("Appartement", "Loft", "Studio")
            val floors = listOf("rez-de-chaussée", "1er étage", "2ème étage", "dernier étage")

            return "${types.random()} - ${floors.random()}"
        }

        fun generateApartmentDescription(): String {
            val attributes = listOf(
                "spacieux", "lumineux", "chaleureux", "élégant", "moderne", "raffiné",
                "avec parquet d'origine", "moulures et cheminées décoratives",
                "grandes fenêtres offrant une belle lumière naturelle", "balcon filant",
                "vue sur la cour intérieure", "cuisine ouverte entièrement équipée",
                "salle de bain moderne avec baignoire et double vasque"
            )

            val selectedAttributes = attributes.shuffled().take(Random.nextInt(4, 7))

            return "Charmant appartement haussmannien, " +
                    selectedAttributes.joinToString(", ") +
                    ". Idéal pour profiter du charme parisien et d'un confort moderne."
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
            val centerLat = 48.8566
            val centerLng = 2.3522

            for (i in 1..10) {
                Log.d(TAG, "Create property : $i")
                // Location
                val locationId = locationDAO.insert(
                    LocationDTO(
                        id = null,
                        latitude = centerLat + randomOffset(),
                        longitude = centerLng + randomOffset(),
                        street = "Rue ${randomStreetNumber()} ${randomStreetName()}",
                        streetNumber = Random.nextInt(1, 20),
                        city = "Paris",
                        postalCode = "750${Random.nextInt(1, 20)}",
                        country = "France"
                    )
                )

                // Property
                val propertyName = generateApartmentName()
                val propertyDescription = generateApartmentDescription()
                val typeId = 1L // RealEstateType id
                val agentId = if (i % 2 == 0) 1L else 2L
                val surface = Random.nextInt(20, 150).toDouble()
                val pricePerM2 = Random.nextInt(8000, 15000)
                val price = surface * pricePerM2
                val rooms = Random.nextInt(1, 5)
                val bathrooms = Random.nextInt(1, 3)
                val bedrooms = if (rooms > 1) Random.nextInt(1, rooms) else 1
                val creationDate = randomDaysBack(365)
                val isSold = false
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

                // Pictures (4 per property)
                val converter = BitmapConverter()

                // image in res/drawable
                // image_1 > exterior.jpg
                // image_2 > living_room.jpg
                // image_3 > bedroom.jpg
                // image_4 > bathroom.jpg
                for (order in 1..4) {
                    val resourceName = "image_${order}"
                    val resId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)

                    if (resId != 0) {
                        val bitmap = BitmapFactory.decodeResource(context.resources, resId)
                        val content = converter.fromBitmap(bitmap)
                        val thumbnailContent = converter.fromBitmap(bitmap)

                        val pictureDTO = PictureDTO(
                            id = null,
                            content = content ?: ByteArray(0),
                            thumbnailContent = thumbnailContent ?: ByteArray(0),
                            order = order,
                            propertyId = propertyId
                        )

                        pictureDAO.insert(pictureDTO)
                    } else {
                        Log.w("DBInit", "Image ressource $resourceName introuvable !")
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