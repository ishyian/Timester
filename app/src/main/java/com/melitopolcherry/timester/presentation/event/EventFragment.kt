package com.melitopolcherry.timester.presentation.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.melitopolcherry.timester.R
import com.melitopolcherry.timester.core.delegates.parcelableParameters
import com.melitopolcherry.timester.core.extensions.showSelectorDialog
import com.melitopolcherry.timester.core.extensions.toDateString
import com.melitopolcherry.timester.core.extensions.toTimeString
import com.melitopolcherry.timester.core.extensions.viewModelCreator
import com.melitopolcherry.timester.core.presentation.BaseFragment
import com.melitopolcherry.timester.data.model.Event
import com.melitopolcherry.timester.data.model.EventType
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

    private val startDateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        viewModel.onStartDateChanged(year, month, dayOfMonth)
    }

    private val startTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        viewModel.onStartTimeChanged(hourOfDay, minute)
    }

    private val endTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        viewModel.onEndTimeChanged(hourOfDay, minute)
    }

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) = FragmentEventBinding.inflate(
        inflater, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        setupClickListener(
            imageDelete,
            imageSave,
            eventStartDate,
            eventStartTime,
            eventEndTime,
            toolbar.btnBack,
            eventTypeHolder
        )

        imageDelete.isVisible = parameters.isEditMode

        eventDescription.doAfterTextChanged { text ->
            viewModel.onDescriptionChanged(text.toString())
        }

        eventTitle.doAfterTextChanged { text ->
            viewModel.onTitleChanged(text.toString())
        }

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
            R.id.event_type_holder -> {
                onEventTypeClick()
            }
            else -> viewModel.onClick(v)
        }
    }

    private fun onEventTypeClick() {
        showSelectorDialog(getString(R.string.select_event_type_title),
                           EventType.values().map { it.typeName }) { dialogInterface: DialogInterface, i: Int ->
            val eventType = EventType.values()[i]
            binding.eventType.text = eventType.typeName
            viewModel.onEventTypeChanged(eventType.value)
            dialogInterface.dismiss()
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
        eventType.text = EventType.typeOf(event.eventType).typeName
    }

    private fun onStartDateChanged(startDate: LocalDateTime) = with(binding) {
        eventStartTime.text = startDate.toTimeString()
        eventStartDate.text = startDate.toDateString()
    }

    private fun onEndDateChanged(endDate: LocalDateTime) = with(binding) {
        eventEndTime.text = endDate.toTimeString()
    }

    private fun initObservers() = with(viewModel) {
        observe(event, ::onEventLoaded)
        observe(eventEndDate, ::onEndDateChanged)
        observe(eventStartDate, ::onStartDateChanged)
        observe(showMsgError, ::showErrorMessage)
    }
}