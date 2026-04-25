package ru.mironov.appwithmapsample.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import ru.mironov.appwithmapsample.app.MainScreen
import ru.mironov.appwithmapsample.data.cities.models.City
import ru.mironov.appwithmapsample.feature.city_details.ui.CityDetailsScreen
import kotlin.reflect.typeOf

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Destination.Main
    ) {
        composable<Destination.Main> {
            MainScreen(navController)
        }

        composable<Destination.CityDetails>(
            typeMap = mapOf(typeOf<City>() to CityNavType)
        ) { backStackEntry ->
            val dest = backStackEntry.toRoute<Destination.CityDetails>()
            CityDetailsScreen(
                city = dest.city,
                navController = navController,
            )
        }
    }
}