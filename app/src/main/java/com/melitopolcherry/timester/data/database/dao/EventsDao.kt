package com.melitopolcherry.timester.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.melitopolcherry.timester.data.database.entity.Event

@Dao
interface EventsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(item: Event): Long

    @Query("DELETE FROM events")
    fun deleteAllEvents()

    @Query("SELECT * FROM events order by start_date")
    suspend fun getAllEvents(): List<Event>

    @Query("SELECT * FROM events where id = :id")
    suspend fun getEventById(id: Long?): Event

    @Query("DELETE FROM events where id = :id")
    fun deleteEvent(id: Long?)
}