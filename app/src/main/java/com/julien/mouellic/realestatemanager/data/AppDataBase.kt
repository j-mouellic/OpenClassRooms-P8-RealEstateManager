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
        PropertyCommodityCrossRef::class,
        PropertyWithDetailsDTO::class,
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

            // ...

            // Commodities
            val idCommodity1 = commodityDAO.insert(
                CommodityDTO(id = 1, name = "Shop")
            )
            Log.d(TAG, "Commodity 1 inserted with id $idCommodity1")

            val idCommodity2 = commodityDAO.insert(
                CommodityDTO(id = 2, name = "Park")
            )
            Log.d(TAG, "Commodity 2 inserted with id $idCommodity2")

            // ...

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