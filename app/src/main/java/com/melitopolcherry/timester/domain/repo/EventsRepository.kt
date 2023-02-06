package com.melitopolcherry.timester.domain.repo

import com.melitopolcherry.timester.data.model.Event

interface EventsRepository {
    suspend fun getEvents(): List<Event>
    suspend fun createEvent(event: Event)
}