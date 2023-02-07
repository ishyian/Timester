package com.melitopolcherry.timester.presentation.event.adapter.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.melitopolcherry.timester.databinding.AddAttendessItemBinding
import com.melitopolcherry.timester.presentation.event.model.AddAttendeeUiModel

fun addAttendeeAD(
    onAddClick: () -> Unit
) = adapterDelegateViewBinding<AddAttendeeUiModel, Any, AddAttendessItemBinding>(
    { layoutInflater, parent ->
        AddAttendessItemBinding.inflate(layoutInflater, parent, false)
    }
) {
    binding.root.setOnClickListener { onAddClick() }
}