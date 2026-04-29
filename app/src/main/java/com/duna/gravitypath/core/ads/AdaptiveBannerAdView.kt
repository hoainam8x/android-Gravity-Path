package com.duna.gravitypath.core.ads

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdaptiveBannerAdView(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val adWidth = configuration.screenWidthDp

    AndroidView(
        modifier = modifier,
        factory = {
            AdView(context).apply {
                adUnitId = AdConfig.bannerAdUnitId
                setAdSize(adaptiveAdSize(context as Activity, adWidth))
                loadAd(AdRequest.Builder().build())
            }
        },
    )
}

private fun adaptiveAdSize(activity: Activity, widthDp: Int): AdSize =
    AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, widthDp)
