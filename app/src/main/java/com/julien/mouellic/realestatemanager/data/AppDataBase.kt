package com.julien.mouellic.realestatemanager.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.julien.mouellic.realestatemanager.data.entity.*
import com.julien.mouellic.realestatemanager.data.dao.*


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
abstract class AppDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDAO
    abstract fun agentDao(): AgentDAO
    abstract fun locationDao(): LocationDAO
    abstract fun realEstateTypeDao(): RealEstateTypeDAO
    abstract fun commodityDao(): CommodityDAO
    abstract fun pictureDao(): PictureDAO
}