package com.melitopolcherry.timester.presentation.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.melitopolcherry.timester.R
import com.melitopolcherry.timester.core.presentation.BaseFragment
import com.melitopolcherry.timester.core.presentation.EventDecorator
import com.melitopolcherry.timester.data.database.AppDatabase
import com.melitopolcherry.timester.data.model.Event
import com.melitopolcherry.timester.data.model.EventType
import com.melitopolcherry.timester.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(), ICalendarFragment, View.OnClickListener {

    private val viewModel: CalendarViewModel by viewModels()

    private val calendar = Calendar.getInstance()

    @Inject
    lateinit var database: AppDatabase

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCalendarBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        setupClickListener(btnAddEvent)
        observe(viewModel.events) {
            calendarView.addDecorator(
                EventDecorator(
                    Color.RED,
                    it.map { CalendarDay.from(it.startDate?.toLocalDate()) })
            )
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_event -> {
                lifecycleScope.launch {
                    val event = Event(
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
                    )
                    calendar.add(Calendar.DAY_OF_MONTH, 4)
                }

            }
            else -> {
                //Ignore
            }
        }
    }

    override fun onVisibilityLoader(isVisibleLoader: Boolean) = Unit
}