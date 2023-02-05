package com.melitopolcherry.timester.presentation

import com.melitopolcherry.timester.presentation.calendar.CalendarFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {
    object Calendar : SupportAppScreen() {
        override fun getFragment() = CalendarFragment()
    }
}