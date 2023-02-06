package com.melitopolcherry.timester.presentation.calendar

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.melitopolcherry.timester.R
import com.melitopolcherry.timester.core.extensions.isSameDay
import com.melitopolcherry.timester.data.model.Event
import com.melitopolcherry.timester.domain.repo.EventsRepository
import com.melitopolcherry.timester.presentation.calendar.model.EventUiModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.topiichat.core.presentation.platform.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    router: Router
) : BaseViewModel(router), ICalendarViewModel {

    private val _events: MutableLiveData<List<Event>> = MutableLiveData()
    val events: LiveData<List<Event>> = _events

    private val _eventsList: MutableLiveData<List<EventUiModel>> = MutableLiveData()
    val eventsList: LiveData<List<EventUiModel>> = _eventsList

    init {
        viewModelScope.launch {
            val events = eventsRepository.getEvents()
            _events.postValue(events)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_add_event -> {

            }
        }
    }

    override fun onEventClick(even: EventUiModel) {
        //TODO Navigate to event
    }

    fun onDaySelected(calendarDay: CalendarDay) {
        val events =
            _events.value?.filter { event -> calendarDay.date.isSameDay(event.startDate) }
                ?.map { EventUiModel(it) }
        _eventsList.postValue(events)
    }
}