package com.melitopolcherry.timester.presentation.calendar.adapter

import android.annotation.SuppressLint
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.melitopolcherry.timester.databinding.EventItemBinding
import com.melitopolcherry.timester.presentation.calendar.model.EventUiModel
import org.threeten.bp.format.DateTimeFormatter

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
            eventItemTime.text = item.event.startDate?.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
    }
}