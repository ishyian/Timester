package com.melitopolcherry.timester.presentation.event

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.melitopolcherry.timester.R
import com.melitopolcherry.timester.data.model.Event
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

    var startDate: LocalDateTime? = null
    var endDate: LocalDateTime? = null

    var newEvent = Event()

    init {
        Timber.d(parameters.isEditMode.toString())
        if (parameters.isEditMode) {
            viewModelScope.launch {
                val event = eventsRepository.getEventById(parameters.eventId)
                newEvent = event
                startDate = event.startDate
                endDate = event.endDate
                Timber.d(event.toString())
                _event.postValue(event)
            }
        } else {
            startDate = LocalDateTime.now()
            endDate = startDate?.plusHours(1)

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
        }
    }

    private fun onBtnSaveClick() {
        if (parameters.isEditMode) {
            newEvent.startDate = startDate
            newEvent.endDate = endDate
            viewModelScope.launch {
                val id = eventsRepository.createEvent(newEvent)
                Timber.d("Event id $id")
                onClickBack()
            }
        } else {
            newEvent.startDate = startDate
            newEvent.endDate = endDate
            newEvent.title = "Event"
            newEvent.description = "Description"
            viewModelScope.launch {
                val id = eventsRepository.createEvent(newEvent)
                Timber.d("Event id $id")
                onClickBack()
            }
        }
    }

    fun addEvent() {
        viewModelScope.launch {
            /*val event = Event(
                startDate = LocalDateTime.ofInstant(
                    DateTimeUtils.toInstant(calendar),
                    ZoneId.systemDefault()
                ),
                endDate = LocalDateTime.ofInstant(
                    DateTimeUtils.toInstant(calendar),
                    ZoneId.systemDefault()
                ),
                title = "Event title retr",
                description = "Event description",
                timeZone = ZoneId.systemDefault().id,
                eventType = EventType.HOLIDAY.value
            )
            val id = database.eventsDao().insertEvent(event)
            Timber.d("Events add $id $event")
            binding.calendarView.addDecorator(
                EventDecorator(
                    Color.RED,
                    listOf(CalendarDay.from(event.startDate?.toLocalDate()))
                )
            )*/
            //calendar.add(Calendar.DAY_OF_MONTH, 4)
        }
    }

    fun onStartDateChanged(year: Int, month: Int, dayOfMonth: Int) {
        startDate = LocalDateTime.of(year, month + 1, dayOfMonth, startDate?.hour ?: 0, startDate?.minute ?: 0)
        endDate = LocalDateTime.of(year, month + 1, dayOfMonth, endDate?.hour ?: 0, endDate?.minute ?: 0)

    }

    fun onStartTimeChanged(hourOfDay: Int, minute: Int) {
        startDate = LocalDateTime.of(
            requireNotNull(startDate?.year),
            requireNotNull(startDate?.month),
            requireNotNull(startDate?.dayOfMonth),
            hourOfDay,
            minute
        )
    }

    fun onEndTimeChanged(hourOfDay: Int, minute: Int) {
        endDate = LocalDateTime.of(
            requireNotNull(endDate?.year),
            requireNotNull(endDate?.month),
            requireNotNull(endDate?.dayOfMonth),
            hourOfDay,
            minute
        )
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(@Assisted parameters: EventParameters): EventViewModel
    }
}