package com.duna.gravitypath.data.local.json

import android.content.Context

class LevelsJsonDataSource(
    private val context: Context,
) {
    fun loadRawJson(): String =
        context.resources.openRawResource(com.duna.gravitypath.R.raw.levels).bufferedReader().use { it.readText() }
}
