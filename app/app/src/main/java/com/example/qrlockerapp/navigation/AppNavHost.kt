package com.example.qrlockerapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.qrlockerapp.FormScreen
import com.example.qrlockerapp.HomeScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable(
            route = "form/{idTaquilla}/{nombreTaquilla}",
            arguments = listOf(
                navArgument("idTaquilla") { type = NavType.StringType },
                navArgument("nombreTaquilla") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idTaquilla = backStackEntry.arguments?.getString("idTaquilla") ?: ""
            val nombreTaquilla = backStackEntry.arguments?.getString("nombreTaquilla") ?: ""
            FormScreen(idTaquilla = idTaquilla, nombreTaquilla = nombreTaquilla,navController = navController)
        }
    }
}
