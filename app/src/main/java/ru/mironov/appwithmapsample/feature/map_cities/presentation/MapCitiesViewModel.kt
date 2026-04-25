package ru.mironov.appwithmapsample.feature.map_cities.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.mironov.appwithmapsample.core.utils.resource.Resource
import ru.mironov.appwithmapsample.core.utils.resource.map
import ru.mironov.appwithmapsample.data.cities.models.City
import ru.mironov.appwithmapsample.domain.cities.CitiesRepository
import javax.inject.Inject
import kotlin.math.roundToInt

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class MapCitiesViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository,
) :
    ViewModel(),
    ContainerHost<MapCitiesState, MapCitiesSideEffect> {

    override val container = container<MapCitiesState, MapCitiesSideEffect>(MapCitiesState())

    private val locationFlow = MutableStateFlow(container.stateFlow.value.centralLocation)

    init {
        initUpdateLocationFlow()
    }

    private fun initUpdateLocationFlow() {
        viewModelScope.launch {
            locationFlow
                .debounce(800)
                .distinctUntilChanged()
                .onEach { location ->
                    intent { reduce { state.copy(centralLocation = location) } }
                }
                .flatMapConcat { location ->
                    citiesRepository.getCitiesInArea(
                        centerLat = location.latitude,
                        centerLng = location.longitude,
                        radiusMeters = container.stateFlow.value.radiusInMeters
                    )
                }
                .collect { resource ->
                    intent {
                        reduce {
                            val newCities = (resource as? Resource.Success)?.value?.items.orEmpty()
                            state.copy(
                                citiesResource = resource.map { it.items },
                                cities = state.cities + newCities
                            )
                        }
                    }
                }
        }
    }

    fun onVisibleRegionChanged(bounds: LatLngBounds) {
        intent {
            val diagonalMeters = SphericalUtil.computeDistanceBetween(
                bounds.southwest,
                bounds.northeast
            )
            reduce { state.copy(radiusInMeters = (diagonalMeters / 1.5).roundToInt()) }
            locationFlow.value = bounds.center
        }
    }

    fun onCitySelected(city: City) = intent {
        postSideEffect(MapCitiesSideEffect.NavigateToCity(city))
    }
}