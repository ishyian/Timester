package com.melitopolcherry.timester.core

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.melitopolcherry.timester.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
