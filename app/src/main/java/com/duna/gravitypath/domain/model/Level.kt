package com.duna.gravitypath.domain.model

enum class GameStage {
    TUTORIAL,
    MATERIALS,
    ADVANCED,
}

enum class SurfaceMaterial {
    WOOD,
    GLASS,
    RUBBER,
}

data class Vec2(
    val x: Float,
    val y: Float,
)

data class Rect(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
)

data class Barrier(
    val position: Vec2,
    val length: Float,
    val thickness: Float,
    val rotation: Float,
    val material: SurfaceMaterial,
)

data class Goal(
    val center: Vec2,
    val radius: Float,
)

data class ZeroGZone(
    val rect: Rect,
)

data class Fan(
    val position: Vec2,
    val force: Vec2,
)

data class Level(
    val id: Int,
    val name: String,
    val stage: GameStage,
    val ballStart: Vec2,
    val ballRadius: Float,
    val goal: Goal,
    val barriers: List<Barrier>,
    val zeroGZones: List<ZeroGZone>,
    val fans: List<Fan>,
)
