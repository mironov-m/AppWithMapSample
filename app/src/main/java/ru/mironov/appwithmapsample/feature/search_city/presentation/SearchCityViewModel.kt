package ru.mironov.appwithmapsample.feature.search_city.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SearchCityViewModel @Inject constructor() :
    ViewModel(),
    ContainerHost<SearchCityState, SearchCitySideEffect> {

    override val container = container<SearchCityState, SearchCitySideEffect>(SearchCityState())

    fun onIntent(intent: SearchCityIntent) = when (intent) {
        is SearchCityIntent.QueryChanged -> onQueryChanged(intent.query)
        is SearchCityIntent.ClearQuery -> onClearQuery()
        is SearchCityIntent.CitySelected -> onCitySelected(intent.city)
    }

    private fun onQueryChanged(query: String) = intent {
        reduce { state.copy(query = query, error = null) }
        if (query.isBlank()) {
            reduce { state.copy(cities = emptyList()) }
        } else {
            searchCities(query)
        }
    }

    private fun onClearQuery() = intent {
        reduce { state.copy(query = "", cities = emptyList(), error = null) }
    }

    private fun onCitySelected(city: CityItem) = intent {
        postSideEffect(SearchCitySideEffect.NavigateToCity(city.id))
    }

    private fun searchCities(query: String) = intent {
        reduce { state.copy(isLoading = true) }
        // TODO: inject and call use case
        reduce { state.copy(isLoading = false) }
    }
}
