package ru.mironov.appwithmapsample.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.mironov.appwithmapsample.feature.search_city.ui.SearchCityScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Destination.SearchCity.route
    ) {
        composable(Destination.SearchCity.route) {
            SearchCityScreen()
        }
    }
}