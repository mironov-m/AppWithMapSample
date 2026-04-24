package ru.mironov.appwithmapsample.feature.search_city.presentation

sealed interface SearchCitySideEffect {
    data class NavigateToCity(val cityId: Long) : SearchCitySideEffect
    data class ShowError(val message: String) : SearchCitySideEffect
}
