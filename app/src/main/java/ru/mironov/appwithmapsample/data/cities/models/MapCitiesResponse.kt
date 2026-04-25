package ru.mironov.appwithmapsample.data.cities.models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MapCitiesResponse(
    val count: Int,
    val items: List<City>
)