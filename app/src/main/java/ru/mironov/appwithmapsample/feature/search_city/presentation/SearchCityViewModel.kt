package ru.mironov.appwithmapsample.feature.search_city.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.mironov.appwithmapsample.core.utils.resource.Resource
import ru.mironov.appwithmapsample.feature.search_city.domain.CitiesRepository
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchCityViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository,
) :
    ViewModel(),
    ContainerHost<SearchCityState, SearchCitySideEffect> {

    override val container = container<SearchCityState, SearchCitySideEffect>(SearchCityState())

    private val queryFlow = MutableStateFlow("")
    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            queryFlow
                .onEach { query ->
                    intent { reduce { state.copy(query = query) } }
                }
                .debounce(400)
                .distinctUntilChanged()
                .collect { query ->
                    searchJob?.cancel()
                    intent {
                        reduce {
                            state.copy(
                                cities = emptyList(),
                                citiesResponse = null,
                                currentPage = 1,
                            )
                        }
                    }
                    if (query.isNotBlank()) {
                        loadCities(query, page = 1, append = false)
                    }
                }
        }
    }

    private fun loadCities(query: String, page: Int, append: Boolean) {
        searchJob = viewModelScope.launch {
            citiesRepository.searchCities(query, page).collect { resource ->
                intent {
                    reduce {
                        val newCities = when {
                            resource is Resource.Success && append -> state.cities + resource.value.items
                            resource is Resource.Success -> resource.value.items
                            else -> state.cities
                        }
                        state.copy(
                            citiesResponse = resource,
                            cities = newCities,
                            currentPage = if (resource is Resource.Success) page else state.currentPage,
                        )
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

    fun onLoadMore() {
        val state = container.stateFlow.value
        if (state.citiesResponse is Resource.Loading || !state.hasMorePages) return
        loadCities(state.query, state.currentPage + 1, append = true)
    }

    fun onCitySelected(cityId: Long) = intent {
        postSideEffect(SearchCitySideEffect.NavigateToCity(cityId))
    }
}