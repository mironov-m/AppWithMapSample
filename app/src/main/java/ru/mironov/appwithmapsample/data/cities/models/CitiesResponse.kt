package ru.mironov.appwithmapsample.data.cities.models

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