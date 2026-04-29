package com.duna.gravitypath.di

import android.content.Context
import com.duna.gravitypath.data.local.json.LevelsJsonDataSource
import com.duna.gravitypath.data.repository.LevelRepositoryImpl
import com.duna.gravitypath.domain.repository.LevelRepository
import com.duna.gravitypath.domain.usecase.GetLevelsUseCase

class AppContainer(context: Context) {
    private val levelsJsonDataSource = LevelsJsonDataSource(context)
    private val levelRepository: LevelRepository = LevelRepositoryImpl(levelsJsonDataSource)

    val getLevelsUseCase: GetLevelsUseCase = GetLevelsUseCase(levelRepository)
}
