package com.julien.mouellic.realestatemanager.di

import com.julien.mouellic.realestatemanager.data.mapper.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MapperModule {

    @Provides
    fun provideAgentMapper() = AgentMapper()

    // TODO : ajouter mapper
}