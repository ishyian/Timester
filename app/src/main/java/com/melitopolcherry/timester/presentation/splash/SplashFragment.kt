package com.melitopolcherry.timester.presentation.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.melitopolcherry.timester.core.presentation.BaseFragment
import com.melitopolcherry.timester.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(), ISplashFragment {

    private val viewModel: SplashViewModel by viewModels()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSplashBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Handler(Looper.getMainLooper()).postDelayed({ viewModel.toMainScreen() }, 1500)
    }

    override fun onVisibilityLoader(isVisibleLoader: Boolean) = Unit
}