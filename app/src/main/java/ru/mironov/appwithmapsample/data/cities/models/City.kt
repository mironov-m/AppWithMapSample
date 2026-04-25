package ru.mironov.appwithmapsample.data.cities.models

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

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