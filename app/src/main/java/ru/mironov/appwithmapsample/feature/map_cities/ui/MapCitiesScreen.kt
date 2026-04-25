package ru.mironov.appwithmapsample.feature.map_cities.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.mironov.appwithmapsample.core.ui.rememberErrorHandler
import ru.mironov.appwithmapsample.core.utils.formatCountryName
import ru.mironov.appwithmapsample.feature.map_cities.presentation.MapCitiesSideEffect
import ru.mironov.appwithmapsample.feature.map_cities.presentation.MapCitiesViewModel

@Composable
fun MapCitiesScreen(
    viewModel: MapCitiesViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val showError = rememberErrorHandler(snackbarHostState)

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is MapCitiesSideEffect.ShowError -> showError(effect.error)
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(state.centralLocation, 10f)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Row(Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                cameraPositionState = cameraPositionState,
            ) {
                LaunchedEffect(cameraPositionState.isMoving, cameraPositionState.position) {
                    if (!cameraPositionState.isMoving) {
                        val bounds = cameraPositionState.projection?.visibleRegion?.latLngBounds
                        bounds?.let(viewModel::onVisibleRegionChanged)
                    }
                }

                state.cities.forEach { city ->
                    Marker(
                        state = MarkerState(position = LatLng(city.lat, city.lon)),
                        title = city.name,
                        snippet = formatCountryName(city.country),
                    )
                }
            }
        }
    }
}