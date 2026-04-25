package ru.mironov.appwithmapsample.feature.search_city.presentation

import ru.mironov.appwithmapsample.feature.search_city.data.models.City

sealed interface SearchCitySideEffect {
    data class NavigateToCity(val city: City) : SearchCitySideEffect
    data class ShowError(val error: Throwable) : SearchCitySideEffect
}
