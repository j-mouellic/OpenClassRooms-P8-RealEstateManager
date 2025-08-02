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
import android.util.Log
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase


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
    abstract fun propertyCommodityCrossRefDAO(): PropertyCommodityCrossRefDAO

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d(TAG, "onCreate called")
            INSTANCE?.let { database ->
                scope.launch {
                    initDatabase(
                        database.agentDao(),
                        database.commodityDao(),
                        database.realEstateTypeDao(),
                    )
                }
            }
        }

        suspend fun initDatabase(
            agentDao : AgentDAO,
            commodityDAO: CommodityDAO,
            estateTypeDAO: RealEstateTypeDAO,
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
                    .addCallback(AppDatabaseCallback(coroutineScope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}