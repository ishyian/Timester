package com.melitopolcherry.timester.presentation.event.adapter.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.melitopolcherry.timester.data.model.Attendee
import com.melitopolcherry.timester.databinding.AttendeeItemBinding
import com.melitopolcherry.timester.presentation.event.model.AttendeeUiModel

fun attendeeAD(
    onAttendeeClick: (Attendee) -> Unit
) = adapterDelegateViewBinding<AttendeeUiModel, Any, AttendeeItemBinding>(
    { layoutInflater, parent ->
        AttendeeItemBinding.inflate(layoutInflater, parent, false)
    }
) {
    binding.root.setOnClickListener { onAttendeeClick(item.attendee) }
    bind {
        binding.textAttendee.text = item.attendee.displayName.take(2)
    }
}