package ru.mironov.appwithmapsample.feature.search_city.data.models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CitiesResponse(
    val items: List<City>,
    val limit: Int,
    val page: Int,
    val total: Long
)

@Keep
@Serializable
data class City(
    val id: Long,
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val pop: Long
)