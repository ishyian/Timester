package com.melitopolcherry.timester.presentation.event

import com.melitopolcherry.timester.core.presentation.IBaseFragment
import com.melitopolcherry.timester.data.database.entity.Event
import com.melitopolcherry.timester.data.model.Attachment
import com.melitopolcherry.timester.data.model.Attendee
import io.github.farhad.contactpicker.PickedContact
import org.threeten.bp.LocalDateTime

interface IEventFragment : IBaseFragment {
    fun sendInvite(attendee: PickedContact)
    fun onAttendeesLoad(list: List<Attendee>)
    fun onAttachmentsLoad(list: List<Attachment>)
    fun onAttendeeClick(attendee: Attendee)
    fun onAttachmentClick(attachment: Attachment)
    fun onAddAttendeeClick()
    fun onEndDateChanged(endDate: LocalDateTime)
    fun onStartDateChanged(startDate: LocalDateTime)
    fun onEventLoaded(event: Event)
    fun onStartTimeClicked()
    fun onStartDateClicked()
    fun onEventTypeClick()
    fun onAddEventToCalendar(event: Event)
    fun onAddClick()
}