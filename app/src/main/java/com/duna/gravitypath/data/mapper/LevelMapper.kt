package com.duna.gravitypath.data.mapper

import com.duna.gravitypath.data.model.LevelDto
import com.duna.gravitypath.domain.model.Level

fun LevelDto.toDomain(): Level = Level(
    id = id,
    name = name,
)
