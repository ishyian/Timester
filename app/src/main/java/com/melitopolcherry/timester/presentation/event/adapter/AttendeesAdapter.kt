package com.melitopolcherry.timester.presentation.event.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.melitopolcherry.timester.core.adapter.BaseDiffCallback
import com.melitopolcherry.timester.data.model.Attendee
import com.melitopolcherry.timester.presentation.event.adapter.delegates.addAttendeeAD
import com.melitopolcherry.timester.presentation.event.adapter.delegates.attendeeAD

class AttendeesAdapter(
    onAddClick: () -> Unit,
    onAttendeeClick: (Attendee) -> Unit
) : AsyncListDifferDelegationAdapter<Any>(DiffCallback) {

    init {
        delegatesManager
            .addDelegate(addAttendeeAD(onAddClick))
            .addDelegate(attendeeAD(onAttendeeClick))
    }

    private companion object DiffCallback : BaseDiffCallback() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any) = when {
            else -> oldItem.javaClass == newItem.javaClass
        }
    }
}