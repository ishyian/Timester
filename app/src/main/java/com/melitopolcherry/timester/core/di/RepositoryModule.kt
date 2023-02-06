package com.melitopolcherry.timester.core.di

import com.melitopolcherry.timester.core.coroutines.AppDispatchers
import com.melitopolcherry.timester.data.database.AppDatabase
import com.melitopolcherry.timester.data.repo.EventsRepositoryImpl
import com.melitopolcherry.timester.domain.repo.EventsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideEventsRepository(
        database: AppDatabase,
        appDispatchers: AppDispatchers
    ): EventsRepository {
        return EventsRepositoryImpl(
            database = database,
            appDispatchers = appDispatchers
        )
    }
}