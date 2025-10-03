package com.julien.mouellic.realestatemanager.di

import com.julien.mouellic.realestatemanager.data.repository.*
import com.julien.mouellic.realestatemanager.domain.usecase.agent.GetAllAgentsUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.commodity.GetAllCommoditiesUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.loan.LoanCalculatorUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.property.InsertEasyPropertyUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.property.SearchPropertiesUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.property.UpdateEasyPropertyUseCase
import com.julien.mouellic.realestatemanager.domain.usecase.realestatetype.GetAllEstateTypesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * UseCaseModule provides the application use cases through Hilt DI.
 *
 * Purpose:
 * 1. **Separation of Concerns (SOC)**: Each use case encapsulates a single business logic operation,
 *    keeping repositories, UI, and domain logic decoupled.
 * 2. **Dependency Injection (DI)**: Hilt automatically provides the required repositories or other dependencies,
 *    making the use cases available throughout the app without manual instantiation.
 *
 * Use Cases provided:
 * - GetAllAgentsUseCase: fetch all agents from the AgentRepository
 * - SearchPropertiesUseCase: search properties based on filters via PropertyRepository
 * - InsertEasyPropertyUseCase: insert a property with all related data through EasyPropertyRepository
 * - UpdateEasyPropertyUseCase: update a property with all related data through EasyPropertyRepository
 * - LoanCalculatorUseCase: compute mortgage/loan calculations
 * - GetAllCommoditiesUseCase: fetch all commodities from CommodityRepository
 * - GetAllEstateTypesUseCase: fetch all estate types from RealEstateTypeRepository
 */
@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    /** Provides use case to fetch all agents */
    @Provides
    fun provideGetAllAgentsUseCase(agentRepository: AgentRepository) =
        GetAllAgentsUseCase(agentRepository)

    /** Provides use case to search properties with filters */
    @Provides
    fun provideSearchPropertiesUseCase(propertyRepository: PropertyRepository) =
        SearchPropertiesUseCase(propertyRepository)

    /** Provides use case to insert properties including related tables */
    @Provides
    fun provideInsertEasyPropertyUseCase(easyPropertyRepository: EasyPropertyRepository) =
        InsertEasyPropertyUseCase(easyPropertyRepository)

    /** Provides use case to calculate loans */
    @Provides
    fun provideLoanCalculatorUseCase() = LoanCalculatorUseCase()

    /** Provides use case to fetch all commodities */
    @Provides
    fun provideGetAllCommoditiesUseCase(commodityRepository: CommodityRepository) =
        GetAllCommoditiesUseCase(commodityRepository)

    /** Provides use case to fetch all real estate types */
    @Provides
    fun provideEstateTypesUseCase(realEstateTypeRepository: RealEstateTypeRepository) =
        GetAllEstateTypesUseCase(realEstateTypeRepository)

    /** Provides use case to update properties including related tables */
    @Provides
    fun provideUpdateEasyPropertyUseCase(easyPropertyRepository: EasyPropertyRepository) =
        UpdateEasyPropertyUseCase(easyPropertyRepository)
}
