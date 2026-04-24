package ru.mironov.appwithmapsample.feature.search_city.presentation

import ru.mironov.appwithmapsample.core.utils.resource.Resource
import ru.mironov.appwithmapsample.feature.search_city.data.models.CitiesResponse
import ru.mironov.appwithmapsample.feature.search_city.data.models.City

data class SearchCityState(
    val query: String = "",
    val citiesResponse: Resource<CitiesResponse>? = null,
    val cities: List<City> = emptyList(),
    val currentPage: Int = 1,
) {
    val hasMorePages: Boolean
        get() {
            val response = citiesResponse
            if (response !is Resource.Success) return true
            return cities.size < response.value.total
        }
}