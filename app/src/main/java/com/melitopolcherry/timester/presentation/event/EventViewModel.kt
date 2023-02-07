package com.melitopolcherry.timester.presentation.event

import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.melitopolcherry.timester.R
import com.melitopolcherry.timester.data.database.entity.Event
import com.melitopolcherry.timester.data.model.Attachment
import com.melitopolcherry.timester.data.model.Attendee
import com.melitopolcherry.timester.data.model.EventType
import com.melitopolcherry.timester.domain.repo.EventsRepository
import com.topiichat.core.presentation.platform.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.github.farhad.contactpicker.PickedContact
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import ru.terrakok.cicerone.Router
import timber.log.Timber

class EventViewModel @AssistedInject constructor(
    @Assisted private val parameters: EventParameters,
    private val eventsRepository: EventsRepository,
    router: Router
) : BaseViewModel(router), IEventViewModel {

    private val _event: MutableLiveData<Event> = MutableLiveData()
    val event: LiveData<Event> = _event

    private val _addEventToCalendar: MutableLiveData<Event> = MutableLiveData()
    val addEventToCalendar: LiveData<Event> = _addEventToCalendar

    private val _attachmentsList: MutableLiveData<List<Attachment>> = MutableLiveData()
    val attachmentsList: LiveData<List<Attachment>> = _attachmentsList

    private val _attendeesList: MutableLiveData<List<Attendee>> = MutableLiveData()
    val attendeesList: LiveData<List<Attendee>> = _attendeesList

    private val _eventStartDate: MutableLiveData<LocalDateTime> = MutableLiveData()
    val eventStartDate: LiveData<LocalDateTime> = _eventStartDate

    private val _eventEndDate: MutableLiveData<LocalDateTime> = MutableLiveData()
    val eventEndDate: LiveData<LocalDateTime> = _eventEndDate

    private var startDate: LocalDateTime = if (parameters.selectedDate != null) {
        LocalDateTime.of(parameters.selectedDate, LocalTime.now())
    } else {
        LocalDateTime.now()
    }
    private var endDate: LocalDateTime = startDate.plusHours(1)
    private var title: String = ""
    private var description: String = ""
    private var isAllDay: Boolean = false
    private var eventType: String = EventType.REGULAR.value

    private var newEvent = Event()

    private var attachments = arrayListOf<Attachment>()
    private var attendees = arrayListOf<Attendee>()

    private val tokenAttachment = object : TypeToken<ArrayList<Attachment>>() {}.type
    private val tokenAttendee = object : TypeToken<ArrayList<Attendee>>() {}.type

    init {
        if (parameters.isEditMode) {
            viewModelScope.launch {
                val event = eventsRepository.getEventById(parameters.eventId)
                event.startDate?.let { startDate = it }
                event.endDate?.let { endDate = it }
                description = event.description
                eventType = event.eventType
                title = event.title

                Timber.d(event.attachments)
                attachments = Gson().fromJson<ArrayList<Attachment>>(event.attachments, tokenAttachment) ?: ArrayList()
                attendees = Gson().fromJson<ArrayList<Attendee>>(event.attendess, tokenAttendee) ?: ArrayList()
                _event.postValue(event)
                _attachmentsList.postValue(attachments)
                _attendeesList.postValue(attendees)
            }
        } else {
            newEvent.startDate = startDate
            newEvent.endDate = endDate

            _event.postValue(newEvent)
            _attachmentsList.postValue(attachments)
            _attendeesList.postValue(attendees)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.image_save -> {
                onBtnSaveClick()
            }
            R.id.image_view_back -> {
                onClickBack()
            }
            R.id.image_delete -> {
                onDeleteClick()
            }
            R.id.btn_add_to_calendar -> {
                onAddEventToCalendarClick()
            }
        }
    }

    private fun onAddEventToCalendarClick() {
        val event = Event()
        event.startDate = startDate
        event.endDate = endDate
        event.title = title
        event.description = description
        _addEventToCalendar.postValue(event)
    }

    override fun onDeleteClick() {
        viewModelScope.launch {
            eventsRepository.deleteEvent(event.value?.id)
            onClickBack()
        }
    }

    override fun onBtnSaveClick() {
        if (parameters.isEditMode) {
            newEvent.id = event.value?.id ?: 0
            newEvent.startDate = startDate
            newEvent.endDate = endDate
            newEvent.title = title
            newEvent.description = description
            newEvent.eventType = eventType
            newEvent.isAllDay = isAllDay
            newEvent.attachments = Gson().toJson(attachments)
            newEvent.attendess = Gson().toJson(attendees)
            viewModelScope.launch {
                eventsRepository.createEvent(newEvent)
                onClickBack()
            }
        } else {
            if (title.isEmpty() || description.isEmpty()) {
                _showMsgError.postValue("Please fill title and description")
                return
            }
            newEvent.startDate = startDate
            newEvent.endDate = endDate
            newEvent.title = title
            newEvent.description = description
            newEvent.eventType = eventType
            newEvent.isAllDay = isAllDay
            newEvent.attachments = Gson().toJson(attachments)
            newEvent.attendess = Gson().toJson(attendees)
            viewModelScope.launch {
                eventsRepository.createEvent(newEvent)
                onClickBack()
            }
        }
    }

    override fun onStartDateChanged(year: Int, month: Int, dayOfMonth: Int) {
        startDate = LocalDateTime.of(year, month + 1, dayOfMonth, startDate.hour, startDate.minute)
        endDate = LocalDateTime.of(year, month + 1, dayOfMonth, endDate.hour, endDate.minute)
        _eventStartDate.postValue(startDate)

    }

    override fun onStartTimeChanged(hourOfDay: Int, minute: Int) {
        startDate = LocalDateTime.of(
            startDate.year,
            startDate.month,
            startDate.dayOfMonth,
            hourOfDay,
            minute
        )

        _eventStartDate.postValue(startDate)
    }

    override fun onEndTimeChanged(hourOfDay: Int, minute: Int) {
        endDate = LocalDateTime.of(
            endDate.year,
            endDate.month,
            endDate.dayOfMonth,
            hourOfDay,
            minute
        )

        _eventEndDate.postValue(endDate)
    }

    override fun onDescriptionChanged(description: String) {
        this.description = description
    }

    override fun onTitleChanged(title: String) {
        this.title = title
    }

    override fun onEventTypeChanged(eventType: String) {
        this.eventType = eventType
    }

    override fun onAllDayChanged(isAllDay: Boolean) {
        this.isAllDay = isAllDay
        if (isAllDay) {
            startDate = LocalDateTime.of(
                startDate.year,
                startDate.month,
                startDate.dayOfMonth,
                0,
                0
            )

            _eventStartDate.postValue(startDate)

            endDate = LocalDateTime.of(
                endDate.year,
                endDate.month,
                endDate.dayOfMonth,
                23,
                59
            )

            _eventEndDate.postValue(endDate)

        }
    }

    override fun addAttachment(uri: Uri?) {
        attachments.add(Attachment(uri.toString()))
    }

    override fun addAttendee(attendee: PickedContact) {
        attendees.add(Attendee(attendee.name.toString(), attendee.number))
    }

    override fun getInviteText(): String {
        val event = Event()
        event.title = title
        event.startDate = startDate
        event.endDate = endDate
        return event.textInvite
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(@Assisted parameters: EventParameters): EventViewModel
    }
}