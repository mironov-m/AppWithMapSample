package ru.mironov.appwithmapsample.data.cities

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import ru.mironov.appwithmapsample.data.cities.models.CitiesResponse
import javax.inject.Inject

interface CitiesApi {
    suspend fun searchCities(query: String, page: Int, limit: Int): CitiesResponse
}

private const val BASE_URL = "http://dev-dep.tools.urent.tech:8080"

class CitiesApiImpl @Inject constructor(
    private val httpClient: HttpClient
) : CitiesApi {

    override suspend fun searchCities(query: String, page: Int, limit: Int): CitiesResponse =
        withContext(IO) {
            httpClient.get("$BASE_URL/api/cities") {
                parameter("query", query)
                parameter("page", page)
                parameter("limit", limit)
            }.body()
        }
}