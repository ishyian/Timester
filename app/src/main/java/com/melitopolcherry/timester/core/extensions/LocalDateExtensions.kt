package com.melitopolcherry.timester.core.extensions

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

fun LocalDate.isSameDay(dateTime: LocalDateTime?): Boolean {
    return this.year == dateTime?.year &&
        this.month == dateTime.month &&
        this.dayOfMonth == dateTime.dayOfMonth
}

fun LocalDate.isSameDay(date: LocalDate): Boolean {
    return this.year == date.year &&
        this.month == date.month &&
        this.dayOfMonth == date.dayOfMonth
}