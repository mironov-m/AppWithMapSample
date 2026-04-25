package ru.mironov.appwithmapsample.feature.map_cities.data.models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MapCitiesResponse(
    val count: Int,
    val items: List<MapCity>
)

@Keep
@Serializable
data class MapCity(
    val id: Long,
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val pop: Long
)