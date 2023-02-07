package com.melitopolcherry.timester.presentation.splash

import android.view.View
import com.melitopolcherry.timester.presentation.Screens
import com.topiichat.core.presentation.platform.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    router: Router
) : BaseViewModel(router), ISplashViewModel {
    override fun onClick(view: View?) {
        //Ignore
    }

    override fun toMainScreen() {
        navigate(Screens.Calendar, clearBackStack = true)
    }
}