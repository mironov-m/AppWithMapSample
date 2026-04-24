package ru.mironov.appwithmapsample.feature.search_city.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.mironov.appwithmapsample.feature.search_city.data.CitiesApi
import ru.mironov.appwithmapsample.feature.search_city.data.CitiesApiImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class SearchCityModule {

    @Binds
    abstract fun bindCitiesApi(impl: CitiesApiImpl): CitiesApi
}