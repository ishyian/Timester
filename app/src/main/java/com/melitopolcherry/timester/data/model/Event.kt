package com.melitopolcherry.timester.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

@Entity(tableName = "events")
class Event {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "start_date")
    var startDate: LocalDateTime? = null

    @ColumnInfo(name = "end_date")
    var endDate: LocalDateTime? = null

    @ColumnInfo(name = "title")
    var title: String = ""

    @ColumnInfo(name = "description")
    var description: String = ""

    @ColumnInfo(name = "time_zone")
    var timeZone: String = ""

    @ColumnInfo(name = "event_type")
    var eventType: String = EventType.REGULAR.value

    val eventStartTime: String?
        get() = startDate?.format(DateTimeFormatter.ofPattern("HH:mm"))

    val eventEndTime: String?
        get() = endDate?.format(DateTimeFormatter.ofPattern("HH:mm"))

    val eventStartDate: String?
        get() = startDate?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

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

