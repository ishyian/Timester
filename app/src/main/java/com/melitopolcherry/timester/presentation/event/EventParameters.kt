package com.melitopolcherry.timester.presentation.event

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventParameters(
    val isEditMode: Boolean = false,
    val eventId: Long? = null
) : Parcelable