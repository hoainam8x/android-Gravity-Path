package com.duna.gravitypath

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.duna.gravitypath.presentation.navigation.GravityPathNavGraph

@Composable
fun GravityPathApp() {
    val navController = rememberNavController()
    MaterialTheme {
        Surface {
            GravityPathNavGraph(navController = navController)
        }
    }
}
