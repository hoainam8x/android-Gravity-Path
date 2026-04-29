package com.duna.gravitypath.presentation.game.state

import com.duna.gravitypath.core.physics.PhysicsEvent

data class GameEventEffect(
    val shouldStopSimulation: Boolean = false,
    val shouldResetLevel: Boolean = false,
    val shouldClearShatterStatus: Boolean = false,
)

fun PhysicsEvent?.toEventEffect(): GameEventEffect = when (this) {
    PhysicsEvent.GoalReached -> GameEventEffect(shouldStopSimulation = true)
    PhysicsEvent.Crashed -> GameEventEffect(
        shouldStopSimulation = true,
        shouldResetLevel = true,
    )
    is PhysicsEvent.GlassShattered -> GameEventEffect(shouldClearShatterStatus = true)
    null -> GameEventEffect()
}
