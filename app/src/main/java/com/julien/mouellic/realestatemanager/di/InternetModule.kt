package com.julien.mouellic.realestatemanager.di


import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class InternetModule {

    @Provides
    @Singleton
    fun provideInternetAvailabilityContext(@ApplicationContext context: Context): InjectedContext {
        return InjectedContext(context)
    }
}