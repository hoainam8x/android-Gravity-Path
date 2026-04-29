package com.duna.gravitypath.domain.repository

import com.duna.gravitypath.domain.model.Level

interface LevelRepository {
    suspend fun getLevels(): List<Level>
}
