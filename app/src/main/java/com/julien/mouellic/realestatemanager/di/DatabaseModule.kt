package com.julien.mouellic.realestatemanager.di

import android.content.Context
import com.julien.mouellic.realestatemanager.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * AppModule provides app-level dependencies such as the Room database and CoroutineScope.
 *
 * Using Hilt for Dependency Injection (DI) allows:
 * 1. Separation of Concerns (SOC): Consumers like repositories or ViewModels do not need to know
 *    how to create the database or manage coroutines.
 * 2. Singleton management: Ensures only one instance of the database and scope exists throughout
 *    the application.
 * 3. Easy testability and modularity: Dependencies can be mocked or replaced in tests.
 */
@Module
@InstallIn(SingletonComponent::class) // Singleton scope for the whole application
class AppModule {

    // ----------------------------
    // Provides a CoroutineScope for database initialization and other long-running tasks
    // ----------------------------
    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    // SupervisorJob ensures that one failing child coroutine does not cancel the whole scope
    // Dispatchers.Main is used here because AppDatabase.initDatabase might update UI/logs

    // ----------------------------
    // Provides the AppDatabase singleton
    // ----------------------------
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        coroutineScope: CoroutineScope
    ): AppDatabase {
        // Lazily creates the Room database with a callback for initialization
        return AppDatabase.getDatabase(context, coroutineScope)
    }
}
