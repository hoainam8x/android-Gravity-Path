package com.duna.gravitypath.domain.usecase

import com.duna.gravitypath.domain.model.Level
import com.duna.gravitypath.domain.repository.LevelRepository

class GetLevelsUseCase(
    private val levelRepository: LevelRepository,
) {
    suspend operator fun invoke(): List<Level> = levelRepository.getLevels()
}
