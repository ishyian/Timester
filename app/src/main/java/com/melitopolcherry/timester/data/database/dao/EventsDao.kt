package com.melitopolcherry.timester.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.melitopolcherry.timester.data.model.Event

@Dao
interface EventsDao {
    @Insert
    suspend fun insertEvent(item: Event): Long

    @Query("DELETE FROM events")
    fun deleteAllEvents()

    @Query("SELECT * FROM events order by start_ts")
    suspend fun getAllEvents(): List<Event>

    @Delete
    fun deleteEvent(event: Event)
}