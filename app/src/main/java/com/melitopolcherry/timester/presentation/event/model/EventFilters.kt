package com.melitopolcherry.timester.presentation.event.model

import com.melitopolcherry.timester.data.model.EventType

object EventFilters {
    val filtersMap = mutableMapOf<String, Boolean>(
        EventType.REGULAR.typeName to true,
        EventType.MEETING.typeName to true,
        EventType.ACTIVITY.typeName to true,
        EventType.REST.typeName to true,
        EventType.HOLIDAY.typeName to true
    )
}