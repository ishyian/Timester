package com.melitopolcherry.timester.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity(tableName = "events")
class Event {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "start_ts")
    var startDate: LocalDateTime? = null

    @ColumnInfo(name = "end_ts")
    var endDate: LocalDateTime? = null

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "description")
    var description: String = ""

    @ColumnInfo(name = "time_zone")
    var timeZone: String = ""

    @ColumnInfo(name = "event_type")
    var eventType: String = EventType.UNKNOWN.value

    @Ignore
    constructor()

    constructor(
        id: Long,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        title: String,
        description: String,
        timeZone: String,
        eventType: String
    ) {
        this.id = id
        this.startDate = startDate
        this.endDate = endDate
        this.title = title
        this.description = description
        this.timeZone = timeZone
        this.eventType = eventType
    }

    @Ignore
    constructor(
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        title: String,
        description: String,
        timeZone: String,
        eventType: String
    ) {
        this.startDate = startDate
        this.endDate = endDate
        this.title = title
        this.description = description
        this.timeZone = timeZone
        this.eventType = eventType
    }

}