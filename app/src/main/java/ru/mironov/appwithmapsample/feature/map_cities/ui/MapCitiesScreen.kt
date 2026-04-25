package ru.mironov.appwithmapsample.feature.map_cities.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.mironov.appwithmapsample.core.ui.rememberErrorHandler
import ru.mironov.appwithmapsample.core.ui.theme.Dimens
import ru.mironov.appwithmapsample.core.utils.formatCountryName
import ru.mironov.appwithmapsample.data.cities.models.City
import ru.mironov.appwithmapsample.feature.map_cities.presentation.MapCitiesSideEffect
import ru.mironov.appwithmapsample.feature.map_cities.presentation.MapCitiesViewModel

@Composable
fun MapCitiesScreen(
    viewModel: MapCitiesViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val showError = rememberErrorHandler(snackbarHostState)
    var selectedCity by remember { mutableStateOf<City?>(null) }

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is MapCitiesSideEffect.ShowError -> showError(effect.error)
            is MapCitiesSideEffect.NavigateToCity -> selectedCity = effect.city
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(state.centralLocation, 17f)
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
                    CityMarker(
                        city = city,
                        onClick = { viewModel.onCitySelected(city) },
                    )
                }
            }
        }

        selectedCity?.let { city ->
            CityDetailsBottomSheet(
                city = city,
                onDismiss = { selectedCity = null },
            )
        }
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
@GoogleMapComposable
private fun CityMarker(
    city: City,
    onClick: () -> Unit,
) {
    MarkerComposable(
        keys = arrayOf(city.id),
        state = MarkerState(position = LatLng(city.lat, city.lon)),
        title = city.name,
        snippet = formatCountryName(city.country),
        onClick = { onClick(); true },
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            shadowElevation = 2.dp,
        ) {
            Text(
                text = city.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(
                    horizontal = Dimens.PaddingSmall,
                    vertical = 4.dp,
                ),
            )
        }
    }
}