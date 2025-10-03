package com.julien.mouellic.realestatemanager.di

import com.julien.mouellic.realestatemanager.data.AppDatabase
import com.julien.mouellic.realestatemanager.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * DaoModule provides DAO (Data Access Objects) instances for dependency injection.
 *
 * Hilt is used here to implement Dependency Injection (DI), which allows us to:
 * 1. Follow the Separation of Concerns (SOC) principle by decoupling the database access from repositories
 *    and other components.
 * 2. Easily inject DAOs wherever needed without manually creating database instances.
 * 3. Improve testability, maintainability, and modularity.
 */
@Module
@InstallIn(SingletonComponent::class) // Scope: SingletonComponent ensures a single instance per application
class DaoModule {

    // ----------------------------
    // Provides AgentDAO instance
    // ----------------------------
    @Provides
    fun provideAgentDAO(appDatabase: AppDatabase): AgentDAO {
        return appDatabase.agentDao()
    }

    // ----------------------------
    // Provides CommodityDAO instance
    // ----------------------------
    @Provides
    fun provideCommodityDAO(appDatabase: AppDatabase): CommodityDAO {
        return appDatabase.commodityDao()
    }

    // ----------------------------
    // Provides RealEstateTypeDAO instance
    // ----------------------------
    @Provides
    fun provideEstateTypeDAO(appDatabase: AppDatabase): RealEstateTypeDAO {
        return appDatabase.realEstateTypeDao()
    }

    // ----------------------------
    // Provides LocationDAO instance
    // ----------------------------
    @Provides
    fun provideLocationDAO(appDatabase: AppDatabase) = appDatabase.locationDao()

    // ----------------------------
    // Provides PictureDAO instance
    // ----------------------------
    @Provides
    fun providePictureDAO(appDatabase: AppDatabase): PictureDAO {
        return appDatabase.pictureDao()
    }

    // ----------------------------
    // Provides PropertyCommodityCrossRefDAO instance
    // ----------------------------
    @Provides
    fun providePropertyCommodityCrossRefDAO(appDatabase: AppDatabase): PropertyCommodityCrossRefDAO {
        return appDatabase.propertyCommodityCrossRefDAO()
    }

    // ----------------------------
    // Provides PropertyDAO instance
    // ----------------------------
    @Provides
    fun providePropertyDAO(appDatabase: AppDatabase): PropertyDAO {
        return appDatabase.propertyDao()
    }

    // ----------------------------
    // Provides PropertyWithDetailsDAO (full property with relations)
    // ----------------------------
    @Provides
    fun provideFullPropertyDAO(appDatabase: AppDatabase) = appDatabase.propertyWithDetailsDao()
}
