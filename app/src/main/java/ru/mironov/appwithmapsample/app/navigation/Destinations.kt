package ru.mironov.appwithmapsample.app.navigation

import kotlinx.serialization.Serializable
import ru.mironov.appwithmapsample.feature.search_city.data.models.City

sealed interface Destination {

    @Serializable
    data object SearchCity : Destination

    @Serializable
    data class CityDetails(val city: City) : Destination
}