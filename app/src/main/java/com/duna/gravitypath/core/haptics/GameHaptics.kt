package com.duna.gravitypath.core.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class GameHaptics(context: Context) {
    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = context.getSystemService(VibratorManager::class.java)
        manager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    fun objectSelected() = vibrate(18, 80)

    fun rotateSnapTick() = vibrate(14, 90)

    fun ballImpact(intensity: Float) {
        val amp = (intensity.coerceIn(0f, 1f) * 255f).toInt().coerceAtLeast(40)
        vibrate(24, amp)
    }

    fun levelComplete() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(
                VibrationEffect.createWaveform(
                    longArrayOf(0, 30, 40, 45),
                    intArrayOf(0, 120, 0, 200),
                    -1,
                ),
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(60)
        }
    }

    private fun vibrate(durationMs: Long, amplitude: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, amplitude))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(durationMs)
        }
    }
}
