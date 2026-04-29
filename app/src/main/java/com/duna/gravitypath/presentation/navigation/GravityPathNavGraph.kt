package com.duna.gravitypath.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.duna.gravitypath.presentation.game.GameScreen
import com.duna.gravitypath.presentation.home.HomeScreen
import com.duna.gravitypath.presentation.worldmap.WorldMapScreen

@Composable
fun GravityPathNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Home.route,
    ) {
        composable(AppRoute.Home.route) {
            HomeScreen(
                onStart = { navController.navigate(AppRoute.Game.route) },
                onWorldMap = { navController.navigate(AppRoute.WorldMap.route) },
            )
        }
        composable(AppRoute.WorldMap.route) {
            WorldMapScreen(
                onBack = { navController.popBackStack() },
                onEnterLevel = { navController.navigate(AppRoute.Game.route) },
            )
        }
        composable(AppRoute.Game.route) {
            GameScreen(onBack = { navController.popBackStack() })
        }
    }
}
