package com.duna.gravitypath.presentation.worldmap

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class WorldMapUiState(
    val completedLevels: Int = 0,
)

class WorldMapViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WorldMapUiState())
    val uiState: StateFlow<WorldMapUiState> = _uiState.asStateFlow()
}
