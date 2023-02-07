package com.melitopolcherry.timester.presentation.calendar

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.melitopolcherry.timester.R
import com.melitopolcherry.timester.core.extensions.lazyUnsynchronized
import com.melitopolcherry.timester.core.extensions.showMultiChoiceDialog
import com.melitopolcherry.timester.core.presentation.BaseFragment
import com.melitopolcherry.timester.core.presentation.EventDecorator
import com.melitopolcherry.timester.data.database.AppDatabase
import com.melitopolcherry.timester.databinding.FragmentCalendarBinding
import com.melitopolcherry.timester.presentation.calendar.adapter.EventsAdapter
import com.melitopolcherry.timester.presentation.calendar.model.EventHeaderUiModel
import com.melitopolcherry.timester.presentation.calendar.model.EventNoEventsUiModel
import com.melitopolcherry.timester.presentation.event.model.EventFilters
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(), ICalendarFragment, OnDateSelectedListener,
    View.OnClickListener {

    private val viewModel: CalendarViewModel by viewModels()

    @Inject
    lateinit var database: AppDatabase

    private val eventsAdapter by lazyUnsynchronized {
        EventsAdapter(
            onEventClick = viewModel::onEventClick,
            onFiltersClick = ::onFiltersClick
        )
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCalendarBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        setupClickListener(btnAddEvent)
        rvEvents.adapter = eventsAdapter
        calendarView.setOnDateChangedListener(this@CalendarFragment)
        initObservers()
    }

    override fun onResume() = with(binding) {
        super.onResume()
        calendarView.removeDecorators()
        viewModel.loadEvents()
    }

    override fun onClick(v: View?) {
        viewModel.onClick(v)
    }

    override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
        viewModel.onDaySelected(date)
    }

    override fun onVisibilityLoader(isVisibleLoader: Boolean) = Unit

    override fun onFiltersClick() {
        requireContext().showMultiChoiceDialog(
            getString(R.string.select_event_type_to_filter),
            EventFilters.filtersMap
        ) { _: DialogInterface, position: Int, isChecked: Boolean ->
            EventFilters.filtersMap[EventFilters.filtersMap.keys.elementAt(position)] = isChecked
            viewModel.filterEvents()
        }

    }

    private fun initObservers() = with(viewModel) {
        observe(events) {
            binding.calendarView.addDecorator(
                EventDecorator(
                    Color.RED,
                    it.map { CalendarDay.from(it.startDate?.toLocalDate()) })
            )
            if (binding.calendarView.selectedDate != null) {
                onDaySelected(requireNotNull(binding.calendarView.selectedDate))
            }
        }
        observe(eventsList) { list ->
            var items = listOf<Any>(EventHeaderUiModel)
            items = if (list.isNotEmpty()) {
                items.plus(list)
            } else items.plus(EventNoEventsUiModel)
            eventsAdapter.items = items
        }
    }
}