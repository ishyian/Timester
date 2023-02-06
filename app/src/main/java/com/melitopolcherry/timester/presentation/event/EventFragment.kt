package com.melitopolcherry.timester.presentation.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.melitopolcherry.timester.R
import com.melitopolcherry.timester.core.delegates.parcelableParameters
import com.melitopolcherry.timester.core.extensions.viewModelCreator
import com.melitopolcherry.timester.core.presentation.BaseFragment
import com.melitopolcherry.timester.data.model.Event
import com.melitopolcherry.timester.databinding.FragmentEventBinding
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDateTime
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class EventFragment : BaseFragment<FragmentEventBinding>(), IEventFragment, View.OnClickListener {

    @Inject
    lateinit var factory: EventViewModel.AssistedFactory

    private val viewModel by viewModelCreator {
        factory.create(parameters)
    }

    private val parameters by parcelableParameters<EventParameters>()

    private val startDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        viewModel.onStartDateChanged(year, month, dayOfMonth)
    }

    private val startTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        viewModel.onStartTimeChanged(hourOfDay, minute)
    }

    private val endTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        viewModel.onEndTimeChanged(hourOfDay, minute)
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentEventBinding.inflate(
        inflater, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        setupClickListener(imageDelete, imageSave, eventStartDate, eventStartTime, eventEndTime)
        imageDelete.isVisible = parameters.isEditMode
        initObservers()
    }

    override fun onVisibilityLoader(isVisibleLoader: Boolean) = Unit

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.event_start_time -> {
                onStartTimeClicked()
            }
            R.id.event_end_time -> {
                onEndTimeClicked()
            }
            R.id.event_start_date -> {
                onStartDateClicked()
            }
            else -> viewModel.onClick(v)
        }
    }

    private fun onStartDateClicked() {
        val datePicker = DatePickerDialog(
            requireContext(),
            startDateSetListener,
            viewModel.event.value?.startDate?.year ?: LocalDateTime.now().year,
            viewModel.event.value?.startDate?.monthValue?.minus(1) ?: LocalDateTime.now().monthValue.minus(1),
            viewModel.event.value?.startDate?.dayOfMonth ?: LocalDateTime.now().dayOfMonth,
        )

        datePicker.datePicker.firstDayOfWeek = Calendar.MONDAY
        datePicker.show()
    }

    private fun onStartTimeClicked() {
        val timePicker = TimePickerDialog(
            requireContext(),
            startTimeSetListener,
            viewModel.event.value?.startDate?.hour ?: LocalDateTime.now().hour,
            viewModel.event.value?.startDate?.minute ?: LocalDateTime.now().minute,
            true
        )
        timePicker.show()
    }

    private fun onEndTimeClicked() {
        val timePicker = TimePickerDialog(
            requireContext(),
            endTimeSetListener,
            viewModel.event.value?.endDate?.hour ?: LocalDateTime.now().hour,
            viewModel.event.value?.endDate?.minute ?: LocalDateTime.now().minute,
            true
        )
        timePicker.show()
    }

    private fun onEventLoaded(event: Event) = with(binding) {
        eventTitle.setText(event.title)
        eventDescription.setText(event.description)
        eventStartTime.text = event.eventStartTime
        eventStartDate.text = event.eventStartDate
        eventEndTime.text = event.eventEndTime
    }

    private fun initObservers() = with(viewModel) {
        observe(event, ::onEventLoaded)
    }
}