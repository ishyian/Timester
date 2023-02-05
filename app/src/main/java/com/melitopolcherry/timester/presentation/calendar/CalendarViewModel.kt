package com.melitopolcherry.timester.presentation.calendar

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.melitopolcherry.timester.R
import com.melitopolcherry.timester.core.coroutines.AppDispatchers
import com.melitopolcherry.timester.data.database.AppDatabase
import com.melitopolcherry.timester.data.model.Event
import com.topiichat.core.presentation.platform.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val database: AppDatabase,
    private val appDispatchers: AppDispatchers,
    router: Router
) : BaseViewModel(router), ICalendarViewModel {

    private val _events: MutableLiveData<List<Event>> = MutableLiveData()
    val events: LiveData<List<Event>> = _events

    init {
        viewModelScope.launch {
            val events = withContext(appDispatchers.storage) {
                database.eventsDao().getAllEvents()
            }
            _events.postValue(events)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_add_event -> {

            }
        }
    }
}