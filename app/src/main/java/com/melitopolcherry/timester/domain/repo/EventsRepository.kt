package com.melitopolcherry.timester.domain.repo

import com.melitopolcherry.timester.data.model.Event

interface EventsRepository {
    suspend fun getEvents(): List<Event>
    suspend fun createEvent(event: Event): Long
    suspend fun getEventById(id: Long?): Event
    suspend fun deleteEvent(id: Long?)
}