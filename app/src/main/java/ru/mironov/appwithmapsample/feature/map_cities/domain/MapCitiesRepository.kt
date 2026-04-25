package ru.mironov.appwithmapsample.feature.map_cities.domain

import kotlinx.coroutines.flow.Flow
import ru.mironov.appwithmapsample.core.utils.resource.Resource
import ru.mironov.appwithmapsample.core.utils.resource.asResource
import ru.mironov.appwithmapsample.feature.map_cities.data.MapCitiesApi
import ru.mironov.appwithmapsample.feature.map_cities.data.models.MapCitiesResponse
import javax.inject.Inject

class MapCitiesRepository @Inject constructor(
    private val mapCitiesApi: MapCitiesApi,
) {

    fun getCitiesInArea(
        query: String,
        radius: Int,
        centerLat: Double,
        centerLng: Double
    ): Flow<Resource<MapCitiesResponse>> {
        return asResource {
            mapCitiesApi.getCitiesInArea(
                query = query,
                radius = radius,
                centerLat = centerLat,
                centerLng = centerLng,
            )
        }
    }
}