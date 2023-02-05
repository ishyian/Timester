package com.melitopolcherry.timester.data.model

enum class EventType(val value: String) {
    MEETING("meeting"),
    ACTIVITY("activity"),
    REST("rest"),
    HOLIDAY("holiday"),
    UNKNOWN("unknown");

    fun typeOf(value: String): EventType {
        return values().firstOrNull { it.value == value } ?: UNKNOWN
    }
}