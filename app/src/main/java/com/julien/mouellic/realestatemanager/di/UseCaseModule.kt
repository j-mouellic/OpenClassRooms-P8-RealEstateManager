package com.julien.mouellic.realestatemanager.di

import com.julien.mouellic.realestatemanager.data.repository.*
import com.julien.mouellic.realestatemanager.domain.usecase.loan.LoanCalculatorUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    /*@Provides
    fun provideGetAgentByIdUseCase(agentRepository: AgentRepository) = GetAgentByIdUseCase(agentRepository)

    @Provides
    fun provideGetAllAgentsUseCase(agentRepository: AgentRepository) = GetAllAgentsUseCase(agentRepository)

    @Provides
    fun provideInsertAgentUseCase(agentRepository: AgentRepository) = InsertAgentUseCase(agentRepository)

    @Provides
    fun provideSearchPropertiesUseCase(propertyRepository : PropertyRepository) = SearchPropertiesUseCase(propertyRepository)

    @Provides
    fun provideInsertPropertyUseCase(easyPropertyRepository: EasyPropertyRepository) = InsertPropertyUseCase(easyPropertyRepository)*/

    @Provides
    fun provideLoanCalculatorUseCase() = LoanCalculatorUseCase()

    /*@Provides
    fun provideGetAllCommoditiesUseCase(commodityRepository: CommodityRepository) = GetAllCommoditiesUseCase(commodityRepository)

    @Provides
    fun provideEstateTypesUseCase(estateTypeRepository: EstateTypeRepository) = GetAllEstateTypesUseCase(estateTypeRepository)

    @Provides
    fun provideUpdatePropertyUseCase(easyPropertyRepository: EasyPropertyRepository) = UpdatePropertyUseCase(easyPropertyRepository)*/
}