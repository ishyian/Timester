package com.melitopolcherry.timester.data.repo

import com.melitopolcherry.timester.core.coroutines.AppDispatchers
import com.melitopolcherry.timester.data.database.AppDatabase
import com.melitopolcherry.timester.data.model.Event
import com.melitopolcherry.timester.domain.repo.EventsRepository
import kotlinx.coroutines.withContext

class EventsRepositoryImpl(
    private val database: AppDatabase,
    private val appDispatchers: AppDispatchers
) : EventsRepository {

    override suspend fun getEvents(): List<Event> {
        return withContext(appDispatchers.storage) {
            database.eventsDao().getAllEvents()
        }
    }

    override suspend fun createEvent(event: Event) {

    }
}