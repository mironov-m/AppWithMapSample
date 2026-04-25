package ru.mironov.appwithmapsample.data.cities

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import ru.mironov.appwithmapsample.data.cities.models.MapCitiesResponse
import javax.inject.Inject

interface MapCitiesApi {

    suspend fun getCitiesInArea(
        query: String?,
        radius: Int,
        centerLat: Double,
        centerLng: Double
    ): MapCitiesResponse
}

private const val BASE_URL = "http://dev-dep.tools.urent.tech:8080"

class MapCitiesApiImpl @Inject constructor(
    private val httpClient: HttpClient
) : MapCitiesApi {

    override suspend fun getCitiesInArea(
        query: String?,
        radius: Int,
        centerLat: Double,
        centerLng: Double
    ): MapCitiesResponse = withContext(IO) {
        httpClient.get("$BASE_URL/api/cities/map") {
            parameter("query", query)
            parameter("radius", radius)
            parameter("centerLat", centerLat)
            parameter("centerLng", centerLng)
        }.body()
    }
}