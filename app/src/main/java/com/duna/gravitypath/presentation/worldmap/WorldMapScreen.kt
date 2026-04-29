package com.duna.gravitypath.presentation.worldmap

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duna.gravitypath.R

@Composable
fun WorldMapScreen(
    onBack: () -> Unit,
    onEnterLevel: () -> Unit,
    viewModel: WorldMapViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = stringResource(id = R.string.map_title))
        Text(text = "${stringResource(id = R.string.map_completed)}: ${state.completedLevels}")
        Button(onClick = onEnterLevel) { Text(text = stringResource(id = R.string.map_enter)) }
        Button(onClick = onBack) { Text(text = stringResource(id = R.string.nav_home)) }
    }
}
