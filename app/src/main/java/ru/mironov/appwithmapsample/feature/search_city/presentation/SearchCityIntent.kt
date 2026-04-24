package ru.mironov.appwithmapsample.feature.search_city.presentation

sealed interface SearchCityIntent {
    data class QueryChanged(val query: String) : SearchCityIntent
    data object ClearQuery : SearchCityIntent
    data class CitySelected(val city: CityItem) : SearchCityIntent
}