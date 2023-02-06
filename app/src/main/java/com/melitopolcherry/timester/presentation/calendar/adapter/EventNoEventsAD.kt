package com.melitopolcherry.timester.presentation.calendar.adapter

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.melitopolcherry.timester.databinding.EventNoEventsBinding
import com.melitopolcherry.timester.presentation.calendar.model.EventNoEventsUiModel

fun eventNoEventsAD() = adapterDelegateViewBinding<EventNoEventsUiModel, Any, EventNoEventsBinding>(
    { layoutInflater, parent ->
        EventNoEventsBinding.inflate(layoutInflater, parent, false)
    }
) {}