package com.melitopolcherry.timester.presentation.calendar.adapter

import android.annotation.SuppressLint
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.melitopolcherry.timester.R
import com.melitopolcherry.timester.data.model.EventType
import com.melitopolcherry.timester.databinding.EventItemBinding
import com.melitopolcherry.timester.presentation.calendar.model.EventUiModel

@SuppressLint("SetTextI18n")
fun eventItemAD(
    onClick: (EventUiModel) -> Unit
) = adapterDelegateViewBinding<EventUiModel, Any, EventItemBinding>(
    { layoutInflater, parent ->
        EventItemBinding.inflate(layoutInflater, parent, false)
    }
) {
    itemView.setOnClickListener {
        onClick(item)
    }
    bind {
        with(binding) {
            eventItemTitle.text = item.event.title
            eventItemDescription.text = item.event.description
            eventItemTime.text = if (item.event.isAllDay) {
                getString(R.string.all_day)
            } else {
                "Start time: ${item.event.eventStartTime}"
            }
            eventItemType.text = EventType.typeOf(item.event.eventType).typeName
        }
    }
}