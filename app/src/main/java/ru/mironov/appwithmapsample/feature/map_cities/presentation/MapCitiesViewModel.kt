package ru.mironov.appwithmapsample.feature.map_cities.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.mironov.appwithmapsample.core.utils.resource.map
import ru.mironov.appwithmapsample.domain.cities.CitiesRepository
import javax.inject.Inject

@HiltViewModel
class MapCitiesViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository,
) :
    ViewModel(),
    ContainerHost<MapCitiesState, MapCitiesSideEffect> {

    override val container = container<MapCitiesState, MapCitiesSideEffect>(MapCitiesState())

    init {
        initCitiesRequest()
    }

    private fun initCitiesRequest() {
        intent {
            citiesRepository.getCitiesInArea(
                centerLat = state.centralLocation.latitude,
                centerLng = state.centralLocation.longitude,
            ).collect { resource ->
                intent {
                    reduce { state.copy(cities = resource.map { it.items }) }
                }
            }
        }
    }
}