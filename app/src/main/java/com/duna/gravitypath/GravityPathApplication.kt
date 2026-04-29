package com.duna.gravitypath

import android.app.Application
import com.duna.gravitypath.di.AppContainer
import com.google.android.gms.ads.MobileAds

class GravityPathApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
        MobileAds.initialize(this)
    }
}
