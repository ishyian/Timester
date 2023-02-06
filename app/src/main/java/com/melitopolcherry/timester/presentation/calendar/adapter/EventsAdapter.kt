package com.melitopolcherry.timester.presentation.calendar.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.melitopolcherry.timester.core.adapter.BaseDiffCallback
import com.melitopolcherry.timester.presentation.calendar.model.EventUiModel

class EventsAdapter(
    onEventClick: (EventUiModel) -> Unit
) : AsyncListDifferDelegationAdapter<Any>(DiffCallback) {

    init {
        delegatesManager
            .addDelegate(eventItemAD(onEventClick))
    }

    private companion object DiffCallback : BaseDiffCallback() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any) = when {
            else -> oldItem.javaClass == newItem.javaClass
        }
    }
}