package com.duna.gravitypath.presentation.game.state

enum class GameStatus {
    IDLE,
    RUNNING,
    GOAL,
    CRASH,
    GLASS_SHATTERED,
}

data class GameUiState(
    val level: Int = 1,
    val levelName: String = "",
    val moves: Int = 0,
    val ballX: Float = 0f,
    val ballY: Float = 0f,
    val status: GameStatus = GameStatus.IDLE,
)
