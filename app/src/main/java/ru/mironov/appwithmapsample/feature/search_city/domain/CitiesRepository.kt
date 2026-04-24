package ru.mironov.appwithmapsample.feature.search_city.domain

import kotlinx.coroutines.flow.Flow
import ru.mironov.appwithmapsample.core.utils.resource.Resource
import ru.mironov.appwithmapsample.core.utils.resource.asResource
import ru.mironov.appwithmapsample.feature.search_city.data.CitiesApi
import ru.mironov.appwithmapsample.feature.search_city.data.models.CitiesResponse
import javax.inject.Inject

private const val PAGE_LIMIT = 10

class CitiesRepository @Inject constructor(
    private val citiesApi: CitiesApi,
) {

    fun searchCities(query: String, page: Int): Flow<Resource<CitiesResponse>> {
        return asResource {
            citiesApi.searchCities(
                query = query,
                page = page,
                limit = PAGE_LIMIT,
            )
        }
    }
}