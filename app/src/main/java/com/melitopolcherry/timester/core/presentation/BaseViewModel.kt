package com.topiichat.core.presentation.platform

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.melitopolcherry.timester.core.presentation.IBaseViewModel
import com.melitopolcherry.timester.core.presentation.SingleLiveData
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen

/**
 * Base ViewModel class .
 * @see ViewModel
 */
abstract class BaseViewModel(private val router: Router) : ViewModel(), IBaseViewModel {

    abstract fun onClick(view: View?)

    protected val _showLoader: MutableLiveData<Boolean> = MutableLiveData()
    val showLoader: LiveData<Boolean> = _showLoader

    protected val _showMsgError: SingleLiveData<String> = SingleLiveData()
    val showMsgError: LiveData<String> = _showMsgError

    protected fun navigate(screen: Screen, clearBackStack: Boolean = false) {
        if (clearBackStack) router.newRootScreen(screen)
        else router.navigateTo(screen)
    }

    protected fun backTo(screen: Screen) {
        router.backTo(screen)
    }

    override fun onClickBack() {
        router.exit()
    }
}