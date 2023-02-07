package com.melitopolcherry.timester.presentation.event

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.threeten.bp.LocalDate

@Parcelize
data class EventParameters(
    val isEditMode: Boolean = false,
    val eventId: Long? = null,
    val selectedDate: LocalDate? = null
) : Parcelable