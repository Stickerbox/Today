package com.stickebox.today.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationBarItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
    val index: Int
)

sealed class Screens(val routeName: String) {
    data object Home : Screens("/home")
    data object Food : Screens("/food")
    data object History : Screens("/history")
    data object AddFood : Screens("/add-food")
}

val BOTTOM_NAVIGATION_BAR_ITEMS = listOf(
    BottomNavigationBarItem(
        label = "Today",
        icon = Icons.Filled.Home,
        route = Screens.Home.routeName,
        index = 0
    ),
    BottomNavigationBarItem(
        label = "Food",
        icon = Icons.Filled.ShoppingCart,
        route = Screens.Food.routeName,
        index = 1
    ),
    BottomNavigationBarItem(
        label = "History",
        icon = Icons.Filled.DateRange,
        route = Screens.History.routeName,
        index = 2
    )
)
