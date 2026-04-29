package com.duna.gravitypath.presentation.home

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
import com.duna.gravitypath.core.ads.AdaptiveBannerAdView

@Composable
fun HomeScreen(
    onStart: () -> Unit,
    onWorldMap: () -> Unit,
    viewModel: HomeViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(id = R.string.home_tagline))
            Text(text = "${stringResource(id = R.string.home_progress)} ${state.currentLevel}")
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = onStart) { Text(text = stringResource(id = R.string.home_play)) }
            Button(onClick = onWorldMap) { Text(text = stringResource(id = R.string.home_worldmap)) }
        }

        AdaptiveBannerAdView()
    }
}
