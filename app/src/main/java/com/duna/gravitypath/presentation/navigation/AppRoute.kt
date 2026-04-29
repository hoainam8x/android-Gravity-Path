package com.duna.gravitypath.presentation.navigation

sealed class AppRoute(val route: String) {
    data object Home : AppRoute("home")
    data object WorldMap : AppRoute("world_map")
    data object Game : AppRoute("game")
}
