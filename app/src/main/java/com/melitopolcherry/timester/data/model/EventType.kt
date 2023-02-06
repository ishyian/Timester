package com.melitopolcherry.timester.data.model

enum class EventType(val value: String, val typeName: String) {
    REGULAR("regular", "Regular event"),
    MEETING("meeting", "Meeting"),
    ACTIVITY("activity", "Activity"),
    REST("rest", "Rest"),
    HOLIDAY("holiday", "Holiday");

    companion object {
        fun typeOf(value: String): EventType {
            return values().firstOrNull { it.value == value } ?: REGULAR
        }

        fun withName(name: String): String {
            return values().firstOrNull { it.name == name }?.value ?: REGULAR.value
        }
    }
}
