package com.duna.gravitypath.presentation.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duna.gravitypath.GravityPathApplication
import com.duna.gravitypath.core.physics.BallState
import com.duna.gravitypath.core.physics.GameLoopController
import com.duna.gravitypath.core.physics.SimplePhysicsEngine
import com.duna.gravitypath.domain.model.Level
import com.duna.gravitypath.domain.model.Vec2
import com.duna.gravitypath.presentation.game.state.GameStatus
import com.duna.gravitypath.presentation.game.state.GameUiState
import com.duna.gravitypath.presentation.game.state.reduceTick
import com.duna.gravitypath.presentation.game.state.toEventEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val getLevelsUseCase = (application as GravityPathApplication).appContainer.getLevelsUseCase
    private val physicsEngine = SimplePhysicsEngine()
    private val gameLoopController = GameLoopController(physicsEngine)
    private var currentLevel: Level? = null
    private var ballState: BallState? = null
    private var simulationJob: Job? = null
    private var accumulatorSeconds: Float = 0f

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val firstLevel = getLevelsUseCase().firstOrNull() ?: return@launch
            currentLevel = firstLevel
            ballState = BallState(
                position = firstLevel.ballStart,
                velocity = Vec2(0f, 0f),
            )
            _uiState.value = _uiState.value.copy(
                level = firstLevel.id,
                levelName = firstLevel.name,
                ballX = firstLevel.ballStart.x,
                ballY = firstLevel.ballStart.y,
                status = GameStatus.IDLE,
            )
        }
    }

    fun onReleaseBall() {
        startSimulation()
    }

    fun startSimulation() {
        if (simulationJob?.isActive == true) return
        simulationJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(status = GameStatus.RUNNING)
            while (isActive) {
                val level = currentLevel ?: break
                val state = ballState ?: break
                val tick = gameLoopController.tick(level, state, accumulatorSeconds)
                accumulatorSeconds = tick.accumulatorSeconds
                ballState = tick.state

                _uiState.value = _uiState.value.reduceTick(tick)
                val effect = tick.event.toEventEffect()

                if (effect.shouldStopSimulation && !effect.shouldResetLevel) {
                    simulationJob = null
                    break
                }
                if (effect.shouldResetLevel) {
                    delay(200)
                    resetLevel()
                    simulationJob = null
                    break
                }
                if (effect.shouldClearShatterStatus) {
                    _uiState.value = _uiState.value.copy(status = GameStatus.RUNNING)
                }

                delay(16)
            }
        }
    }

    fun resetLevel() {
        simulationJob?.cancel()
        simulationJob = null
        accumulatorSeconds = 0f
        val level = currentLevel ?: return
        ballState = BallState(position = level.ballStart, velocity = Vec2(0f, 0f))
        _uiState.value = _uiState.value.copy(
            moves = 0,
            ballX = level.ballStart.x,
            ballY = level.ballStart.y,
            status = GameStatus.IDLE,
        )
    }

    override fun onCleared() {
        simulationJob?.cancel()
        super.onCleared()
    }
}
