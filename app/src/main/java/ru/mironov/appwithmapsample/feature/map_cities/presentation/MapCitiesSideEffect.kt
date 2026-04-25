package ru.mironov.appwithmapsample.feature.map_cities.presentation

sealed interface MapCitiesSideEffect {

    data class ShowError(val error: Throwable) : MapCitiesSideEffect
}