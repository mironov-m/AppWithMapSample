package ru.mironov.appwithmapsample.feature.map_cities.presentation

import ru.mironov.appwithmapsample.data.cities.models.City

sealed interface MapCitiesSideEffect {

    data class ShowError(val error: Throwable) : MapCitiesSideEffect

    data class NavigateToCity(val city: City) : MapCitiesSideEffect
}