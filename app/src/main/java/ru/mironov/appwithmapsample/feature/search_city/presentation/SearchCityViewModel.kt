package ru.mironov.appwithmapsample.feature.search_city.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.mironov.appwithmapsample.core.utils.resource.Resource
import ru.mironov.appwithmapsample.core.utils.resource.asResource
import ru.mironov.appwithmapsample.feature.search_city.data.models.City
import ru.mironov.appwithmapsample.feature.search_city.domain.CitiesRepository
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchCityViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository,
) :
    ViewModel(),
    ContainerHost<SearchCityState, SearchCitySideEffect> {

    override val container = container<SearchCityState, SearchCitySideEffect>(SearchCityState())

    private val queryFlow = MutableStateFlow("")

    init {
        viewModelScope.launch {
            queryFlow
                .onEach { query ->
                    intent {
                        reduce { state.copy(query = query) }
                    }
                }
                .debounce(400)
                .distinctUntilChanged()
                .flatMapLatest<String, Resource<List<City>>> { query ->
                    if (query.isBlank()) {
                        flowOf(emptyList<City>())
                            .asResource()
                    } else {
                        citiesRepository.searchCities(query, page = 1)
                    }
                }
                .collect { result ->
                    intent {
                        reduce {
                            state.copy(cities = result)
                        }
                    }
                }
        }
    }


    fun onQueryChanged(query: String) {
        queryFlow.value = query
    }

    fun onClearQuery() {
        queryFlow.value = ""
    }

    fun onCitySelected(cityId: Long) = intent {
        postSideEffect(SearchCitySideEffect.NavigateToCity(cityId))
    }
}
