package ru.mironov.appwithmapsample.feature.map_cities.presentation

import com.google.android.gms.maps.model.LatLng
import ru.mironov.appwithmapsample.core.utils.resource.Resource
import ru.mironov.appwithmapsample.data.cities.models.City

private val locationMoscow = LatLng(55.754133, 37.6194807)

data class MapCitiesState(
    val centralLocation: LatLng = locationMoscow,
    val citiesResource: Resource<List<City>>? = null,
    val cities: Set<City> = emptySet(),
    val radiusInMeters: Int = 50_000,
)
