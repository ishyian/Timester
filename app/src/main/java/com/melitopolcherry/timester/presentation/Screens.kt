package com.melitopolcherry.timester.presentation

import com.melitopolcherry.timester.core.delegates.parcelableParametersBundleOf
import com.melitopolcherry.timester.presentation.calendar.CalendarFragment
import com.melitopolcherry.timester.presentation.event.EventFragment
import com.melitopolcherry.timester.presentation.event.EventParameters
import ru.terrakok.cicerone.android.support.FragmentParams
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {
    object Calendar : SupportAppScreen() {
        override fun getFragment() = CalendarFragment()
    }

    class Event(
        private val parameters: EventParameters
    ) : SupportAppScreen() {
        override fun getFragment() = EventFragment()
        override fun getFragmentParams() = FragmentParams(
            EventFragment::class.java,
            parcelableParametersBundleOf(parameters)
        )
    }
}