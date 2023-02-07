package com.melitopolcherry.timester.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.melitopolcherry.timester.data.model.EventType
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

    @ColumnInfo(name = "isAllDay")
    var isAllDay: Boolean = false

    @ColumnInfo(name = "attachments")
    var attachments: String = ""

    @ColumnInfo(name = "attendees")
    var attendess: String = ""

    val eventStartTime: String?
        get() = startDate?.format(DateTimeFormatter.ofPattern("HH:mm"))

    val eventEndTime: String?
        get() = endDate?.format(DateTimeFormatter.ofPattern("HH:mm"))

    val eventStartDate: String?
        get() = startDate?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

    val textInvite: String
        get() = "I invite you to $title on $eventStartDate at $eventStartTime. Please RSVP my invite via reply."

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

