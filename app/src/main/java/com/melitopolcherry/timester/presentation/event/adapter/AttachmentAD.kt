package com.melitopolcherry.timester.presentation.event.adapter

import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.melitopolcherry.timester.data.model.Attachment
import com.melitopolcherry.timester.databinding.AttachmentItemBinding
import com.melitopolcherry.timester.presentation.event.model.AttachmentUiModel

fun attachmentAD(
    onAttachmentClick: (Attachment) -> Unit
) = adapterDelegateViewBinding<AttachmentUiModel, Any, AttachmentItemBinding>(
    { layoutInflater, parent ->
        AttachmentItemBinding.inflate(layoutInflater, parent, false)
    }
) {
    binding.root.setOnClickListener { onAttachmentClick(item.attachment) }
    bind {
        val uri = item.attachment.uri.toUri()
        Glide.with(itemView.context).load(uri).into(binding.imageAttachment)
    }
}