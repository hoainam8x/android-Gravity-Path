package com.duna.gravitypath.presentation.game.state

import com.duna.gravitypath.core.physics.LoopTickResult

fun GameUiState.reduceTick(tick: LoopTickResult): GameUiState {
    if (tick.consumedSteps <= 0 && tick.event == null) return this
    return copy(
        moves = moves + tick.consumedSteps,
        ballX = tick.state.position.x,
        ballY = tick.state.position.y,
        status = tick.event.toGameStatus(),
    )
}
