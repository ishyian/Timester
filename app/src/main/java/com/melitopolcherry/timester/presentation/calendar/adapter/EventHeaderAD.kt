package com.melitopolcherry.timester.presentation.calendar.adapter

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.melitopolcherry.timester.databinding.EventHeaderBinding
import com.melitopolcherry.timester.presentation.calendar.model.EventHeaderUiModel

fun eventHeaderAD(
    onFiltersClick: () -> Unit
) = adapterDelegateViewBinding<EventHeaderUiModel, Any, EventHeaderBinding>(
    { layoutInflater, parent ->
        EventHeaderBinding.inflate(layoutInflater, parent, false)
    }
) {
    binding.imageSort.setOnClickListener { onFiltersClick() }
}