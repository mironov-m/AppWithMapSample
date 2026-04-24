package ru.mironov.appwithmapsample.feature.search_city.presentation

import ru.mironov.appwithmapsample.core.utils.resource.Resource
import ru.mironov.appwithmapsample.feature.search_city.data.models.City

data class SearchCityState(
    val query: String = "",
    val cities: Resource<List<City>> = Resource.Success(emptyList()),
)
