package com.duna.gravitypath.core.ads

import com.duna.gravitypath.BuildConfig

object AdConfig {
    const val APP_ID = "ca-app-pub-5115828862032559~1080516094"

    private const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/2934735716"
    private const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

    // TODO: Fill production IDs before release.
    private const val REAL_BANNER_AD_UNIT_ID = "TODO_REAL_BANNER_AD_UNIT_ID"
    private const val REAL_INTERSTITIAL_AD_UNIT_ID = "TODO_REAL_INTERSTITIAL_AD_UNIT_ID"

    val bannerAdUnitId: String
        get() = if (BuildConfig.DEBUG) TEST_BANNER_AD_UNIT_ID else REAL_BANNER_AD_UNIT_ID

    val interstitialAdUnitId: String
        get() = if (BuildConfig.DEBUG) TEST_INTERSTITIAL_AD_UNIT_ID else REAL_INTERSTITIAL_AD_UNIT_ID
}
