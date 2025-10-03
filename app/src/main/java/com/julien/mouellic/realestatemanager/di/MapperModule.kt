package com.julien.mouellic.realestatemanager.di

import com.julien.mouellic.realestatemanager.data.mapper.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * MapperModule provides mapper instances via Hilt for dependency injection.
 *
 * Purpose:
 * 1. **Separation of Concerns (SOC)**: Keeps the mapping logic between DTOs and domain models
 *    in dedicated mapper classes, avoiding clutter in repositories or UI code.
 * 2. **Dependency Injection (DI)**: Provides singleton-like access to these mappers wherever needed
 *    without manually instantiating them.
 *
 * Mappers provided:
 * - AgentMapper: converts between AgentDTO and Agent model
 * - CommodityMapper: converts between CommodityDTO and Commodity model
 * - PictureMapper: converts between PictureDTO and Picture model
 * - PropertyMapper: converts between PropertyDTO and Property model
 * - PropertyWithDetailsMapper: converts between complex property queries (with related entities) and Property model
 * - RealEstateTypeMapper: converts between RealEstateTypeDTO and RealEstateType model
 */
@Module
@InstallIn(SingletonComponent::class)
class MapperModule {

    /** Provide AgentMapper instance */
    @Provides
    fun provideAgentMapper() = AgentMapper()

    /** Provide CommodityMapper instance */
    @Provides
    fun provideCommodityMapper() = CommodityMapper()

    /** Provide PictureMapper instance */
    @Provides
    fun providePictureMapper() = PictureMapper()

    /** Provide PropertyMapper instance */
    @Provides
    fun providePropertyMapper() = PropertyMapper()

    /** Provide PropertyWithDetailsMapper instance */
    @Provides
    fun providePropertyWithDetailsMapper() = PropertyWithDetailsMapper()

    /** Provide RealEstateTypeMapper instance */
    @Provides
    fun provideRealEstateTypeMapper() = RealEstateTypeMapper()
}
