package com.melitopolcherry.timester.presentation.calendar

import com.melitopolcherry.timester.presentation.calendar.model.EventUiModel

interface ICalendarViewModel {
    fun onEventClick(even: EventUiModel)
}