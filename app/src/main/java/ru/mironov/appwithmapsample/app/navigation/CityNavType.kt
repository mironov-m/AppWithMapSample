package ru.mironov.appwithmapsample.app.navigation

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import ru.mironov.appwithmapsample.data.cities.models.City

val CityNavType = object : NavType<City>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): City? =
        bundle.getString(key)?.let { Json.decodeFromString(it) }

    override fun parseValue(value: String): City = Json.decodeFromString(value)

    override fun serializeAsValue(value: City): String = Json.encodeToString(City.serializer(), value)

    override fun put(bundle: Bundle, key: String, value: City) =
        bundle.putString(key, Json.encodeToString(City.serializer(), value))
}