package com.example.mybooks.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mybooks.ui.screens.AddEditBookScreen
import com.example.mybooks.ui.screens.HomeScreen

@Composable
fun Navigation(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "Home") {
        composable(route = "Home") {
            HomeScreen(navController)
        }

        composable(route = "AddEdit/{bookId}") { backStackEntry ->
            AddEditBookScreen(navController, bookId = backStackEntry.arguments?.getString("bookId"))
        }
    }
}



