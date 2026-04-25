package ru.mironov.appwithmapsample.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.mironov.appwithmapsample.app.navigation.BottomTab
import ru.mironov.appwithmapsample.feature.map_cities.ui.MapCitiesScreen
import ru.mironov.appwithmapsample.feature.search_city.ui.SearchCityScreen

private data class BottomNavItem(
    val tab: BottomTab,
    val icon: ImageVector,
    val contentDescription: String,
)

private val bottomNavItems = listOf(
    BottomNavItem(BottomTab.CityList, Icons.AutoMirrored.Filled.List, "City list"),
    BottomNavItem(BottomTab.CityMap, Icons.Default.Map, "City map"),
)

@Composable
fun MainScreen(outerNavController: NavHostController) {
    val innerNavController = rememberNavController()
    val backStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                bottomNavItems.forEach { item ->
                    val selected = currentDestination?.hasRoute(item.tab::class) == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            innerNavController.navigate(item.tab) {
                                popUpTo(innerNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.contentDescription,
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.secondary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = Color.Transparent,
                        ),
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = innerNavController,
            startDestination = BottomTab.CityList,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<BottomTab.CityList> {
                SearchCityScreen(outerNavController)
            }
            composable<BottomTab.CityMap> {
                MapCitiesScreen(outerNavController)
            }
        }
    }
}