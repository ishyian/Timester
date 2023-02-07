package com.melitopolcherry.timester.presentation.event

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.melitopolcherry.timester.MainActivity
import com.melitopolcherry.timester.R
import com.melitopolcherry.timester.core.contract.PickFileActivityContract
import com.melitopolcherry.timester.core.delegates.parcelableParameters
import com.melitopolcherry.timester.core.extensions.lazyUnsynchronized
import com.melitopolcherry.timester.core.extensions.showSelectorDialog
import com.melitopolcherry.timester.core.extensions.toDateString
import com.melitopolcherry.timester.core.extensions.toTimeString
import com.melitopolcherry.timester.core.extensions.viewModelCreator
import com.melitopolcherry.timester.core.permissions.AndroidPermissionsManager
import com.melitopolcherry.timester.core.presentation.BaseFragment
import com.melitopolcherry.timester.data.database.converters.LocalDateTypeConverter
import com.melitopolcherry.timester.data.database.entity.Event
import com.melitopolcherry.timester.data.model.Attachment
import com.melitopolcherry.timester.data.model.Attendee
import com.melitopolcherry.timester.data.model.EventType
import com.melitopolcherry.timester.databinding.FragmentEventBinding
import com.melitopolcherry.timester.presentation.event.adapter.AttachmentsAdapter
import com.melitopolcherry.timester.presentation.event.adapter.AttendeesAdapter
import com.melitopolcherry.timester.presentation.event.model.AddAttachmentUiModel
import com.melitopolcherry.timester.presentation.event.model.AddAttendeeUiModel
import com.melitopolcherry.timester.presentation.event.model.AttachmentUiModel
import com.melitopolcherry.timester.presentation.event.model.AttendeeUiModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.farhad.contactpicker.ContactPicker
import io.github.farhad.contactpicker.PickedContact
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

    private val attachmentsAdapter by lazyUnsynchronized {
        AttachmentsAdapter(
            onAddClick = ::onAddClick,
            onAttachmentsClick = ::onAttachmentClick
        )
    }

    private val attendeeAdapter by lazyUnsynchronized {
        AttendeesAdapter(
            onAddClick = ::onAddAttendeeClick,
            onAttendeeClick = ::onAttendeeClick
        )
    }

    private val pickFile = registerForActivityResult(PickFileActivityContract()) { uri ->
        attachmentsAdapter.items = attachmentsAdapter
            .items
            .plus(AttachmentUiModel(Attachment(uri.toString())))
        viewModel.addAttachment(uri)
    }

    private var permissionManager: AndroidPermissionsManager? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionManager = AndroidPermissionsManager(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        setupClickListener(
            imageDelete,
            imageSave,
            eventStartDate,
            eventStartTime,
            eventEndTime,
            toolbar.btnBack,
            eventTypeHolder,
            btnAddToCalendar
        )

        imageDelete.isVisible = parameters.isEditMode

        eventDescription.doAfterTextChanged { text ->
            viewModel.onDescriptionChanged(text.toString())
        }

        eventTitle.doAfterTextChanged { text ->
            viewModel.onTitleChanged(text.toString())
        }

        eventAllDay.setOnCheckedChangeListener { _, isChecked ->
            eventEndTime.isInvisible = isChecked
            eventStartTime.isInvisible = isChecked
            eventStartTimeTitle.isInvisible = isChecked
            eventEndTimeTitle.isInvisible = isChecked
            viewModel.onAllDayChanged(isChecked)
        }

        rvAttachments.adapter = attachmentsAdapter
        rvAttendees.adapter = attendeeAdapter

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

    override fun onAddEventToCalendar(event: Event) {
        val startMillis = LocalDateTypeConverter().dateToTimestamp(event.startDate)
        val endMillis = LocalDateTypeConverter().dateToTimestamp(event.endDate)
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
            .putExtra(CalendarContract.Events.TITLE, event.title)
            .putExtra(CalendarContract.Events.DESCRIPTION, event.description)
            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
        startActivity(intent)
    }

    override fun onEventTypeClick() {
        showSelectorDialog(getString(R.string.select_event_type_title),
                           EventType.values().map { it.typeName }) { dialogInterface: DialogInterface, i: Int ->
            val eventType = EventType.values()[i]
            binding.eventType.text = eventType.typeName
            viewModel.onEventTypeChanged(eventType.value)
            dialogInterface.dismiss()
        }
    }

    override fun onStartDateClicked() {
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

    override fun onStartTimeClicked() {
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

    override fun onEventLoaded(event: Event) = with(binding) {
        eventTitle.setText(event.title)
        eventDescription.setText(event.description)
        eventStartTime.text = event.eventStartTime
        eventStartDate.text = event.eventStartDate
        eventEndTime.text = event.eventEndTime
        eventEndTime.isInvisible = event.isAllDay
        eventStartTime.isInvisible = event.isAllDay
        eventAllDay.isChecked = event.isAllDay
        eventType.text = EventType.typeOf(event.eventType).typeName
        btnAddToCalendar.isVisible = parameters.isEditMode
    }

    override fun onStartDateChanged(startDate: LocalDateTime) = with(binding) {
        eventStartTime.text = startDate.toTimeString()
        eventStartDate.text = startDate.toDateString()
    }

    override fun onEndDateChanged(endDate: LocalDateTime) = with(binding) {
        eventEndTime.text = endDate.toTimeString()
    }

    override fun onAddClick() {
        permissionManager?.requestPermissions(
            permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            granted = { pickFile.launch(Unit) },
            denied = { showErrorMessage(getString(R.string.file_permission_denied)) },
            rationale = { showErrorMessage(getString(R.string.file_permission_denied)) }
        )
    }

    override fun onAddAttendeeClick() {
        val picker: ContactPicker? = ContactPicker.create(
            activity = requireActivity() as MainActivity,
            onContactPicked = { attendee ->
                attendeeAdapter.items = attendeeAdapter
                    .items
                    .plus(AttendeeUiModel(Attendee(attendee.name.toString(), attendee.number)))
                viewModel.addAttendee(attendee)
                sendInvite(attendee)
            },
            onFailure = { error ->
                showErrorMessage(
                    error.localizedMessage?.toString() ?: getString(R.string.contact_picker_error)
                )
            })

        picker?.pick()
    }

    override fun onAttachmentClick(attachment: Attachment) {
        val view = Intent(Intent.ACTION_VIEW)
        view.data = attachment.uri.toUri()
        view.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            startActivity(view)
        } catch (e: ActivityNotFoundException) {
            showErrorMessage(getString(R.string.no_application_found_to_open_file))
        } catch (e: SecurityException) {
            showErrorMessage(getString(R.string.sharing_application_not_grant_permission))
        }
    }

    override fun onAttendeeClick(attendee: Attendee) {
        sendInvite(PickedContact(attendee.phoneNumber, attendee.displayName))
    }

    override fun onAttachmentsLoad(list: List<Attachment>) {
        attachmentsAdapter.items = listOf(AddAttachmentUiModel).plus(list.map { AttachmentUiModel(it) })
    }

    override fun onAttendeesLoad(list: List<Attendee>) {
        attendeeAdapter.items = listOf(AddAttendeeUiModel).plus(list.map { AttendeeUiModel(it) })
    }

    override fun sendInvite(attendee: PickedContact) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:${attendee.number}") // This ensures only SMS apps respond
        intent.putExtra("sms_body", viewModel.getInviteText())
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            try {
                startActivity(intent)
            } catch (anfe: ActivityNotFoundException) {
                //Ignore
            }
        }
    }

    private fun initObservers() = with(viewModel) {
        observe(event, ::onEventLoaded)
        observe(eventEndDate, ::onEndDateChanged)
        observe(eventStartDate, ::onStartDateChanged)
        observe(showMsgError, ::showErrorMessage)
        observe(attachmentsList, ::onAttachmentsLoad)
        observe(attendeesList, ::onAttendeesLoad)
        observe(addEventToCalendar, ::onAddEventToCalendar)
    }
}