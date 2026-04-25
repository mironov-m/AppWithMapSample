package ru.mironov.appwithmapsample.feature.map_cities.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.mironov.appwithmapsample.data.cities.MapCitiesApi
import ru.mironov.appwithmapsample.data.cities.MapCitiesApiImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class MapCitiesModule {

    @Binds
    abstract fun bindMapCitiesApi(impl: MapCitiesApiImpl): MapCitiesApi
}