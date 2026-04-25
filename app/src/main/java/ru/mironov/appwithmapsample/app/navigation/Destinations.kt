package ru.mironov.appwithmapsample.app.navigation

import kotlinx.serialization.Serializable
import ru.mironov.appwithmapsample.data.cities.models.City

sealed interface Destination {

    @Serializable
    data object Main : Destination

    @Serializable
    data class CityDetails(val city: City) : Destination
}

sealed interface BottomTab {

    @Serializable
    data object CityList : BottomTab

    @Serializable
    data object CityMap : BottomTab
}