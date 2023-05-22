package com.example.mybooks.ui.navigation

const val DETAIL_ARGUMENT_KEY = "bookId"
sealed class Screen (val route: String) {
    object HomeScreen : Screen("Home")
    object AddEditBookScreen : Screen("AddEditBook/{$DETAIL_ARGUMENT_KEY}") {
        fun withId(id: String): String {
            return this.route.replace(oldValue = "{$DETAIL_ARGUMENT_KEY}", newValue = id)
        }
    }
}