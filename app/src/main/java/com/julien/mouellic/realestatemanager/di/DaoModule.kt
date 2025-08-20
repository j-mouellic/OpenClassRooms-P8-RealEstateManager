package com.julien.mouellic.realestatemanager.di

import com.julien.mouellic.realestatemanager.data.AppDatabase
import com.julien.mouellic.realestatemanager.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {
    // DAO

    @Provides
    fun provideAgentDAO(appDatabase: AppDatabase): AgentDAO {
        return appDatabase.agentDao()
    }

    @Provides
    fun provideCommodityDAO(appDatabase: AppDatabase): CommodityDAO {
        return appDatabase.commodityDao()
    }

    @Provides
    fun provideEstateTypeDAO(appDatabase: AppDatabase): RealEstateTypeDAO {
        return appDatabase.realEstateTypeDao()
    }

    @Provides
    fun provideLocationDAO(appDatabase: AppDatabase) = appDatabase.locationDao()

    @Provides
    fun providePictureDAO(appDatabase: AppDatabase): PictureDAO {
        return appDatabase.pictureDao()
    }

    @Provides
    fun providePropertyCommodityCrossRefDAO(appDatabase: AppDatabase): PropertyCommodityCrossRefDAO {
        return appDatabase.propertyCommodityCrossRefDAO()
    }

    @Provides
    fun providePropertyDAO(appDatabase: AppDatabase): PropertyDAO {
        return appDatabase.propertyDao()
    }

    @Provides
    fun provideFullPropertyDAO(appDatabase: AppDatabase) = appDatabase.propertyWithDetailsDao()

}