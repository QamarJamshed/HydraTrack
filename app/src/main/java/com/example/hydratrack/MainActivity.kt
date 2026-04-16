package com.example.hydratrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hydratrack.ui.MainViewModel
import com.example.hydratrack.ui.achievements.AchievementsScreen
import com.example.hydratrack.ui.assistant.AssistantScreen
import com.example.hydratrack.ui.dashboard.DashboardScreen
import com.example.hydratrack.ui.onboarding.OnboardingScreen
import com.example.hydratrack.ui.settings.SettingsScreen
import com.example.hydratrack.ui.theme.HydraTrackTheme
import com.example.hydratrack.ui.trends.TrendsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val themeMode by viewModel.themeMode.collectAsState()
            
            HydraTrackTheme(themeMode = themeMode) {
                HydraTrackAppContent(viewModel)
            }
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Home", Icons.Default.Dashboard)
    object Trends : Screen("trends", "Trends", Icons.Default.BarChart)
    object Assistant : Screen("assistant", "AI Coach", Icons.Default.SmartToy)
    object Achievements : Screen("achievements", "Badges", Icons.Default.EmojiEvents)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun HydraTrackAppContent(
    viewModel: MainViewModel
) {
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsState()

    if (isOnboardingCompleted) {
        MainAppNavigation()
    } else {
        OnboardingScreen(onComplete = {
            // Flow will update
        })
    }
}

@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Dashboard,
        Screen.Trends,
        Screen.Assistant,
        Screen.Achievements,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) { DashboardScreen() }
            composable(Screen.Trends.route) { TrendsScreen() }
            composable(Screen.Assistant.route) { AssistantScreen() }
            composable(Screen.Achievements.route) { AchievementsScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
        }
    }
}
