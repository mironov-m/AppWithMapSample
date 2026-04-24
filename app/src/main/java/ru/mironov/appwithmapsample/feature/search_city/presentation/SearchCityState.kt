package ru.mironov.appwithmapsample.feature.search_city.presentation

data class SearchCityState(
    val query: String = "",
    val cities: List<CityItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null
)

data class CityItem(
    val id: Long,
    val name: String,
    val country: String,
)
