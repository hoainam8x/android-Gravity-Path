package com.duna.gravitypath

import android.app.Application
import com.duna.gravitypath.di.AppContainer
import com.google.android.gms.ads.MobileAds
import java.util.concurrent.Executors

class GravityPathApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
        Executors.newSingleThreadExecutor().execute {
            MobileAds.initialize(this@GravityPathApplication) {}
        }
    }
}
