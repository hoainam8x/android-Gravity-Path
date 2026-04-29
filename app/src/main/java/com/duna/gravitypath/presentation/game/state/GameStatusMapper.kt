package com.duna.gravitypath.presentation.game.state

import com.duna.gravitypath.core.physics.PhysicsEvent

fun PhysicsEvent?.toGameStatus(): GameStatus = when (this) {
    PhysicsEvent.GoalReached -> GameStatus.GOAL
    PhysicsEvent.Crashed -> GameStatus.CRASH
    is PhysicsEvent.GlassShattered -> GameStatus.GLASS_SHATTERED
    null -> GameStatus.RUNNING
}
