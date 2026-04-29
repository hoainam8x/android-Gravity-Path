package com.duna.gravitypath.presentation.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duna.gravitypath.R
import com.duna.gravitypath.core.ads.AdaptiveBannerAdView
import com.duna.gravitypath.presentation.game.state.GameStatus

@Composable
fun GameScreen(
    onBack: () -> Unit,
    viewModel: GameViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val statusText = when (state.status) {
        GameStatus.IDLE -> stringResource(id = R.string.game_status_idle)
        GameStatus.RUNNING -> stringResource(id = R.string.game_status_running)
        GameStatus.GOAL -> stringResource(id = R.string.game_status_goal)
        GameStatus.CRASH -> stringResource(id = R.string.game_status_crash)
        GameStatus.GLASS_SHATTERED -> stringResource(id = R.string.game_status_glass_shattered)
    }

    LaunchedEffect(state.level) {
        viewModel.startSimulation()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "${stringResource(id = R.string.hud_level)} ${state.level}")
            Text(text = state.levelName)
            Text(text = "${stringResource(id = R.string.hud_moves)} ${state.moves}")
            Text(text = stringResource(id = R.string.game_ball_position, state.ballX, state.ballY))
            Text(text = statusText)
            Text(text = stringResource(id = R.string.hud_tap_release))
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = viewModel::onReleaseBall) {
                Text(text = stringResource(id = R.string.hud_tap_release))
            }
            Button(onClick = viewModel::resetLevel) {
                Text(text = stringResource(id = R.string.nav_restart))
            }
            Button(onClick = onBack) { Text(text = stringResource(id = R.string.nav_home)) }
        }

        AdaptiveBannerAdView()
    }
}
