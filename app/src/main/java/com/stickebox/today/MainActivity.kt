package com.stickebox.today

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stickebox.common.generateRandomColor
import com.stickebox.food.FoodScreen
import com.stickebox.food.NavigationEvent
import com.stickebox.home.ui.HomeScreen
import com.stickebox.today.domain.BOTTOM_NAVIGATION_BAR_ITEMS
import com.stickebox.today.domain.Screens
import com.stickebox.uitheme.theme.TodayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()

            TodayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainActivityView(navController)
                }
            }
        }
    }
}

@Composable
fun MainActivityView(
    navController: NavHostController
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var componentHeight by remember { mutableStateOf(0.dp) }
    var shouldNavItemsBeDark by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val todayColors by remember { mutableStateOf(listOf(generateRandomColor())) }
    val historyColors by remember { mutableStateOf(listOf(generateRandomColor())) }

    Box {
        NavHost(
            navController = navController,
            startDestination = Screens.Home.routeName,
        ) {
            composable(Screens.Home.routeName) {
                HomeScreen(
                    listModifier = Modifier.padding(bottom = componentHeight),
                    colors = todayColors,
                    isBottomDark = {
                        shouldNavItemsBeDark = it
                    },
                    viewModel = viewModel()
                )
            }
            composable(Screens.Food.routeName) {
                FoodScreen(
                    listModifier = Modifier.padding(bottom = componentHeight),
                    isBottomDark = {
                        shouldNavItemsBeDark = !it
                    },
                    viewModel = viewModel(),
                    onNavigationEvent = {
                        when (it) {
                            is NavigationEvent.AddFood -> {
                                // Show camera view
                            }
                        }
                    }
                )
            }
            composable(Screens.History.routeName) {
                HomeScreen(
                    listModifier = Modifier.padding(bottom = componentHeight),
                    colors = historyColors,
                    isBottomDark = {
                        shouldNavItemsBeDark = it
                    },
                    viewModel = viewModel()
                )
            }
        }

        NavigationBar(
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        1.0f to Color.DarkGray.copy(alpha = 0.7f)
                    )
                )
                .onGloballyPositioned {
                    componentHeight = with(density) {
                        it.size.height.toDp()
                    }
                }
        ) {
            val color = if (shouldNavItemsBeDark) Color.DarkGray else Color.White
            BOTTOM_NAVIGATION_BAR_ITEMS.map {
                NavigationBarItem(
                    selected = selectedTabIndex == it.index,
                    label = {
                        Text(text = it.label, color = color)
                    },
                    onClick = {
                        selectedTabIndex = it.index
                        navController.navigate(it.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(it.icon, contentDescription = it.label, tint = color)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val navHostController = rememberNavController()
    MainActivityView(navHostController)
}
