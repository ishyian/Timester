package com.melitopolcherry.timester.presentation.event.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.melitopolcherry.timester.core.adapter.BaseDiffCallback
import com.melitopolcherry.timester.data.model.Attachment

class AttachmentsAdapter(
    onAddClick: () -> Unit,
    onAttachmentsClick: (Attachment) -> Unit
) : AsyncListDifferDelegationAdapter<Any>(DiffCallback) {

    init {
        delegatesManager
            .addDelegate(addAttachmentAD(onAddClick))
            .addDelegate(attachmentAD(onAttachmentsClick))
    }

    private companion object DiffCallback : BaseDiffCallback() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any) = when {
            else -> oldItem.javaClass == newItem.javaClass
        }
    }
}