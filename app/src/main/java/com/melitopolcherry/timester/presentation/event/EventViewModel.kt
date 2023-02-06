package com.melitopolcherry.timester.presentation.event

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.melitopolcherry.timester.R
import com.melitopolcherry.timester.data.model.Event
import com.melitopolcherry.timester.data.model.EventType
import com.melitopolcherry.timester.domain.repo.EventsRepository
import com.topiichat.core.presentation.platform.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import ru.terrakok.cicerone.Router
import timber.log.Timber

class EventViewModel @AssistedInject constructor(
    @Assisted private val parameters: EventParameters,
    private val eventsRepository: EventsRepository,
    router: Router
) : BaseViewModel(router), IEventViewModel {

    private val _event: MutableLiveData<Event> = MutableLiveData()
    val event: LiveData<Event> = _event

    private val _eventStartDate: MutableLiveData<LocalDateTime> = MutableLiveData()
    val eventStartDate: LiveData<LocalDateTime> = _eventStartDate

    private val _eventEndDate: MutableLiveData<LocalDateTime> = MutableLiveData()
    val eventEndDate: LiveData<LocalDateTime> = _eventEndDate

    private var startDate: LocalDateTime = LocalDateTime.now()
    private var endDate: LocalDateTime = startDate.plusHours(1)
    private var title: String = ""
    private var description: String = ""
    private var isAllDay: Boolean = false
    private var eventType: String = EventType.REGULAR.value

    private var newEvent = Event()

    init {
        Timber.d(parameters.isEditMode.toString())
        if (parameters.isEditMode) {
            viewModelScope.launch {
                val event = eventsRepository.getEventById(parameters.eventId)

                event.startDate?.let { startDate = it }
                event.endDate?.let { endDate = it }
                description = event.description
                eventType = event.eventType
                title = event.title

                _event.postValue(event)
            }
        } else {
            newEvent.startDate = startDate
            newEvent.endDate = endDate

            _event.postValue(newEvent)
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
        }
    }

    private fun onDeleteClick() {
        viewModelScope.launch {
            eventsRepository.deleteEvent(event.value?.id)
            onClickBack()
        }
    }

    private fun onBtnSaveClick() {
        if (parameters.isEditMode) {
            newEvent.id = event.value?.id ?: 0
            newEvent.startDate = startDate
            newEvent.endDate = endDate
            newEvent.title = title
            newEvent.description = description
            newEvent.eventType = eventType
            newEvent.isAllDay = isAllDay
            viewModelScope.launch {
                val id = eventsRepository.createEvent(newEvent)
                Timber.d("Event id $id")
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
            viewModelScope.launch {
                val id = eventsRepository.createEvent(newEvent)
                Timber.d("Event id $id")
                onClickBack()
            }
        }
    }

    fun onStartDateChanged(year: Int, month: Int, dayOfMonth: Int) {
        startDate = LocalDateTime.of(year, month + 1, dayOfMonth, startDate.hour, startDate.minute)
        endDate = LocalDateTime.of(year, month + 1, dayOfMonth, endDate.hour, endDate.minute)

        _eventStartDate.postValue(startDate)

    }

    fun onStartTimeChanged(hourOfDay: Int, minute: Int) {
        startDate = LocalDateTime.of(
            startDate.year,
            startDate.month,
            startDate.dayOfMonth,
            hourOfDay,
            minute
        )

        _eventStartDate.postValue(startDate)
    }

    fun onEndTimeChanged(hourOfDay: Int, minute: Int) {
        endDate = LocalDateTime.of(
            endDate.year,
            endDate.month,
            endDate.dayOfMonth,
            hourOfDay,
            minute
        )

        _eventEndDate.postValue(endDate)
    }

    fun onDescriptionChanged(description: String) {
        this.description = description
    }

    fun onTitleChanged(title: String) {
        this.title = title
    }

    fun onEventTypeChanged(eventType: String) {
        this.eventType = eventType
    }

    fun onAllDayChanged(isAllDay: Boolean) {
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

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(@Assisted parameters: EventParameters): EventViewModel
    }
}