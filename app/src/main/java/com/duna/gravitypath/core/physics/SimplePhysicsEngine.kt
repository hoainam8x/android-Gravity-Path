package com.duna.gravitypath.core.physics

import com.duna.gravitypath.domain.model.Level
import com.duna.gravitypath.domain.model.SurfaceMaterial
import com.duna.gravitypath.domain.model.Vec2
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt

data class BallState(
    val position: Vec2,
    val velocity: Vec2,
    val brokenBarrierIndices: Set<Int> = emptySet(),
    val collisionCooldownFrames: Int = 0,
)

sealed class PhysicsEvent {
    data object GoalReached : PhysicsEvent()
    data object Crashed : PhysicsEvent()
    data class GlassShattered(val barrierIndex: Int) : PhysicsEvent()
}

data class PhysicsStepResult(
    val state: BallState,
    val event: PhysicsEvent? = null,
)

class SimplePhysicsEngine {
    fun step(level: Level, state: BallState, dtSeconds: Float): PhysicsStepResult {
        val substeps = 3
        val subDt = dtSeconds / substeps
        var workingState = state

        for (i in 0 until substeps) {
            val single = stepSingle(level, workingState, subDt)
            workingState = single.state
            if (single.event != null) {
                return PhysicsStepResult(state = workingState, event = single.event)
            }
        }

        return PhysicsStepResult(state = workingState)
    }

    private fun stepSingle(level: Level, state: BallState, dtSeconds: Float): PhysicsStepResult {
        val cooldown = (state.collisionCooldownFrames - 1).coerceAtLeast(0)
        val inZeroG = level.zeroGZones.any { zone ->
            state.position.x in zone.rect.x..(zone.rect.x + zone.rect.width) &&
                state.position.y in zone.rect.y..(zone.rect.y + zone.rect.height)
        }

        val gravity = if (inZeroG) Vec2(0f, 0f) else Vec2(0f, -9.8f)
        val fanForce = level.fans.fold(Vec2(0f, 0f)) { acc, fan ->
            val dx = fan.position.x - state.position.x
            val dy = fan.position.y - state.position.y
            val distance = sqrt(dx * dx + dy * dy).coerceAtLeast(1f)
            if (distance <= 180f) {
                Vec2(acc.x + fan.force.x / distance, acc.y + fan.force.y / distance)
            } else {
                acc
            }
        }

        val goalPull = goalPullForce(level, state.position)
        val acceleration = Vec2(
            gravity.x + fanForce.x + goalPull.x,
            gravity.y + fanForce.y + goalPull.y,
        )
        val uncappedVelocity = Vec2(
            x = state.velocity.x + acceleration.x * dtSeconds,
            y = state.velocity.y + acceleration.y * dtSeconds,
        )
        val newVelocity = capVelocity(uncappedVelocity, maxSpeed = 9.5f)
        val newPosition = Vec2(
            x = state.position.x + newVelocity.x * dtSeconds * 60f,
            y = state.position.y + newVelocity.y * dtSeconds * 60f,
        )

        if (isInsideGoalMouth(level, newPosition)) {
            return PhysicsStepResult(
                state = state.copy(position = newPosition, velocity = Vec2(0f, 0f), collisionCooldownFrames = 0),
                event = PhysicsEvent.GoalReached,
            )
        }

        if (newPosition.y >= 844f || newPosition.x <= 0f || newPosition.x >= 390f) {
            return PhysicsStepResult(
                state = state.copy(position = newPosition, velocity = Vec2(0f, 0f), collisionCooldownFrames = 0),
                event = PhysicsEvent.Crashed,
            )
        }

        val stateWithCooldown = state.copy(collisionCooldownFrames = cooldown)
        val barrierCollision = if (cooldown == 0) {
            collideWithBarrier(level, stateWithCooldown, newPosition, newVelocity)
        } else {
            null
        }
        if (barrierCollision != null) return barrierCollision

        return PhysicsStepResult(
            state = stateWithCooldown.copy(position = newPosition, velocity = newVelocity),
        )
    }

    private fun collideWithBarrier(
        level: Level,
        state: BallState,
        position: Vec2,
        velocity: Vec2,
    ): PhysicsStepResult? {
        level.barriers.forEachIndexed { index, barrier ->
            if (index in state.brokenBarrierIndices) return@forEachIndexed

            val ux = cos(barrier.rotation)
            val uy = sin(barrier.rotation)
            val halfLen = barrier.length * 0.5f
            val halfThick = barrier.thickness * 0.5f

            val dx = position.x - barrier.position.x
            val dy = position.y - barrier.position.y

            val localX = dx * ux + dy * uy
            val localY = -dx * uy + dy * ux

            val clampedX = localX.coerceIn(-halfLen, halfLen)
            val clampedY = localY.coerceIn(-halfThick, halfThick)

            val diffX = localX - clampedX
            val diffY = localY - clampedY
            val distSq = diffX * diffX + diffY * diffY

            if (distSq <= level.ballRadius * level.ballRadius) {
                val normalLocal = if (abs(diffY) > 0.001f || abs(diffX) > 0.001f) {
                    val inv = 1f / sqrt((distSq).coerceAtLeast(0.0001f))
                    Vec2(diffX * inv, diffY * inv)
                } else {
                    Vec2(0f, if (localY >= 0f) 1f else -1f)
                }
                val normalWorld = Vec2(
                    x = normalLocal.x * ux + normalLocal.y * -uy,
                    y = normalLocal.x * uy + normalLocal.y * ux,
                )

                val speedToBarrier = velocity.x * normalWorld.x + velocity.y * normalWorld.y
                val reflected = if (speedToBarrier > 0f) velocity else {
                    val restitution = when (barrier.material) {
                        SurfaceMaterial.WOOD -> 0.18f
                        SurfaceMaterial.GLASS -> 0.40f
                        SurfaceMaterial.RUBBER -> 0.75f
                    }
                    val friction = when (barrier.material) {
                        SurfaceMaterial.WOOD -> 0.25f
                        SurfaceMaterial.GLASS -> 0.08f
                        SurfaceMaterial.RUBBER -> 0.60f
                    }

                    val vn = Vec2(
                        x = speedToBarrier * normalWorld.x,
                        y = speedToBarrier * normalWorld.y,
                    )
                    val vt = Vec2(
                        x = velocity.x - vn.x,
                        y = velocity.y - vn.y,
                    )
                    val reflectedNormal = Vec2(
                        x = -(1f + restitution) * vn.x,
                        y = -(1f + restitution) * vn.y,
                    )
                    val dampedTangent = Vec2(
                        x = vt.x * max(0f, 1f - friction),
                        y = vt.y * max(0f, 1f - friction),
                    )
                    Vec2(
                        x = reflectedNormal.x + dampedTangent.x,
                        y = reflectedNormal.y + dampedTangent.y,
                    )
                }

                if (barrier.material == SurfaceMaterial.GLASS) {
                    val shouldShatter = abs(speedToBarrier) >= 2.1f
                    if (shouldShatter) {
                        return PhysicsStepResult(
                            state = state.copy(
                                position = position,
                                velocity = reflected,
                                brokenBarrierIndices = state.brokenBarrierIndices + index,
                                collisionCooldownFrames = 3,
                            ),
                            event = PhysicsEvent.GlassShattered(index),
                        )
                    }
                }

                return PhysicsStepResult(
                    state = state.copy(
                        position = position,
                        velocity = reflected,
                        collisionCooldownFrames = 2,
                    ),
                )
            }
        }
        return null
    }

    private fun isInsideGoalMouth(level: Level, ballPos: Vec2): Boolean {
        val r = level.goal.radius
        val mouthWidth = r * 1.6f
        val mouthHeight = r * 0.6f
        val center = level.goal.center
        return ballPos.x in (center.x - mouthWidth / 2f)..(center.x + mouthWidth / 2f) &&
            ballPos.y in (center.y - mouthHeight * 0.3f)..(center.y + mouthHeight * 0.3f)
    }

    private fun goalPullForce(level: Level, ballPos: Vec2): Vec2 {
        val center = level.goal.center
        val dx = center.x - ballPos.x
        val dy = center.y - ballPos.y
        val d = sqrt(dx * dx + dy * dy).coerceAtLeast(1f)
        val pullRadius = level.goal.radius * 3.2f
        if (d > pullRadius) return Vec2(0f, 0f)
        val strength = ((pullRadius - d) / pullRadius) * 6.5f
        return Vec2(dx / d * strength, dy / d * strength)
    }

    private fun capVelocity(v: Vec2, maxSpeed: Float): Vec2 {
        val speed = sqrt(v.x * v.x + v.y * v.y)
        if (speed <= maxSpeed) return v
        val scale = maxSpeed / speed
        return Vec2(v.x * scale, v.y * scale)
    }

}
