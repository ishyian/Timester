package com.melitopolcherry.timester.presentation.event.adapter.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.melitopolcherry.timester.databinding.AddAttachmentsItemBinding
import com.melitopolcherry.timester.presentation.event.model.AddAttachmentUiModel

fun addAttachmentAD(
    onAddClick: () -> Unit
) = adapterDelegateViewBinding<AddAttachmentUiModel, Any, AddAttachmentsItemBinding>(
    { layoutInflater, parent ->
        AddAttachmentsItemBinding.inflate(layoutInflater, parent, false)
    }
) {
    binding.root.setOnClickListener { onAddClick() }
}