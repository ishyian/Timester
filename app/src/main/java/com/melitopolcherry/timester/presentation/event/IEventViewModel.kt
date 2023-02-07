package com.melitopolcherry.timester.presentation.event

import android.net.Uri
import com.melitopolcherry.timester.core.presentation.IBaseViewModel
import io.github.farhad.contactpicker.PickedContact

interface IEventViewModel : IBaseViewModel {
    fun getInviteText(): String
    fun addAttendee(attendee: PickedContact)
    fun addAttachment(uri: Uri?)
    fun onAllDayChanged(isAllDay: Boolean)
    fun onEventTypeChanged(eventType: String)
    fun onTitleChanged(title: String)
    fun onDescriptionChanged(description: String)
    fun onEndTimeChanged(hourOfDay: Int, minute: Int)
    fun onStartTimeChanged(hourOfDay: Int, minute: Int)
    fun onStartDateChanged(year: Int, month: Int, dayOfMonth: Int)
    fun onBtnSaveClick()
    fun onDeleteClick()
}