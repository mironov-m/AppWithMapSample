package ru.mironov.appwithmapsample.domain.cities

import kotlinx.coroutines.flow.Flow
import ru.mironov.appwithmapsample.core.utils.resource.Resource
import ru.mironov.appwithmapsample.core.utils.resource.asResource
import ru.mironov.appwithmapsample.data.cities.CitiesApi
import ru.mironov.appwithmapsample.data.cities.MapCitiesApi
import ru.mironov.appwithmapsample.data.cities.models.CitiesResponse
import ru.mironov.appwithmapsample.data.cities.models.MapCitiesResponse
import javax.inject.Inject

private const val PAGE_LIMIT = 10

class CitiesRepository @Inject constructor(
    private val citiesApi: CitiesApi,
    private val mapCitiesApi: MapCitiesApi,
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

    fun getCitiesInArea(
        radiusMeters: Int = 50_000,
        centerLat: Double,
        centerLng: Double
    ): Flow<Resource<MapCitiesResponse>> {
        return asResource {
            mapCitiesApi.getCitiesInArea(
                query = null,
                radius = radiusMeters,
                centerLat = centerLat,
                centerLng = centerLng,
            )
        }
    }
}