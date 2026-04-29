package com.duna.gravitypath.core.physics

import com.duna.gravitypath.domain.model.Level
import kotlin.math.min

data class LoopTickResult(
    val state: BallState,
    val consumedSteps: Int,
    val event: PhysicsEvent?,
    val accumulatorSeconds: Float,
)

class GameLoopController(
    private val physicsEngine: SimplePhysicsEngine,
    private val fixedDt: Float = 1f / 60f,
    private val frameDt: Float = 1f / 60f,
    private val maxAccumulatedSteps: Int = 4,
) {
    fun tick(
        level: Level,
        initialState: BallState,
        accumulatorSeconds: Float,
    ): LoopTickResult {
        var accumulator = min(accumulatorSeconds + frameDt, fixedDt * maxAccumulatedSteps)
        var workingState = initialState
        var lastEvent: PhysicsEvent? = null
        var consumedSteps = 0

        while (accumulator >= fixedDt) {
            val stepped = physicsEngine.step(level, workingState, dtSeconds = fixedDt)
            workingState = stepped.state
            accumulator -= fixedDt
            consumedSteps++
            if (stepped.event != null) {
                lastEvent = stepped.event
                break
            }
        }

        return LoopTickResult(
            state = workingState,
            consumedSteps = consumedSteps,
            event = lastEvent,
            accumulatorSeconds = accumulator,
        )
    }
}
