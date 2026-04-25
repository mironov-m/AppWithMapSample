package ru.mironov.appwithmapsample.feature.search_city.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.mironov.appwithmapsample.core.android.InternetConnectionObserver
import ru.mironov.appwithmapsample.core.network.exception.NoInternetException
import ru.mironov.appwithmapsample.core.utils.resource.Resource
import ru.mironov.appwithmapsample.data.cities.models.CitiesResponse
import ru.mironov.appwithmapsample.data.cities.models.City
import ru.mironov.appwithmapsample.domain.cities.CitiesRepository
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchCityViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository,
    internetConnectionObserver: InternetConnectionObserver,
) :
    ViewModel(),
    ContainerHost<SearchCityState, SearchCitySideEffect> {

    override val container = container<SearchCityState, SearchCitySideEffect>(SearchCityState())

    private val queryFlow = MutableStateFlow("")
    private var searchJob: Job? = null

    init {
        initCitiesRequest()
        initRetryAfterConnectionActivated(internetConnectionObserver)
    }

    private fun initCitiesRequest() {
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
                                cities = emptySet(),
                                citiesResponse = null,
                                currentPage = 1,
                            )
                        }
                    }
                    if (query.isNotBlank()) {
                        loadCities(query = query, page = 1, append = false)
                    }
                }
        }
    }

    private fun initRetryAfterConnectionActivated(internetConnectionObserver: InternetConnectionObserver) {
        viewModelScope.launch {
            internetConnectionObserver.observe()
                .filter { connectionAvailable -> connectionAvailable }
                .collect {
                    intent {
                        val error = (state.citiesResponse as? Resource.Error<CitiesResponse>)?.throwable
                        if (error is NoInternetException) {
                            loadCities(
                                query = state.query,
                                page = state.currentPage + 1,
                                append = state.cities.isNotEmpty(),
                            )
                        }
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
                            resource is Resource.Success -> resource.value.items.toSet()
                            else -> state.cities
                        }
                        state.copy(
                            citiesResponse = resource,
                            cities = newCities,
                            currentPage = if (resource is Resource.Success) page else state.currentPage,
                        )
                    }
                    if (resource is Resource.Error) {
                        postSideEffect(SearchCitySideEffect.ShowError(resource.throwable))
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

    fun onCitySelected(city: City) = intent {
        postSideEffect(SearchCitySideEffect.NavigateToCity(city))
    }
}