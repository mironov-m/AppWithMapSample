package ru.mironov.appwithmapsample.feature.map_cities.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.mironov.appwithmapsample.R
import ru.mironov.appwithmapsample.core.android.searchInBrowser
import ru.mironov.appwithmapsample.core.ui.CityInfoFields
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
                    Marker(
                        state = MarkerState(position = LatLng(city.lat, city.lon)),
                        title = city.name,
                        snippet = formatCountryName(city.country),
                        onClick = { viewModel.onCitySelected(city); true }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CityDetailsBottomSheet(
    city: City,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.PaddingMedium)
                .padding(bottom = Dimens.PaddingLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium),
        ) {
            Text(
                text = stringResource(R.string.city_details_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            CityInfoFields(city = city)
            Button(
                onClick = { searchInBrowser(context, city.name) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.PaddingSmall),
                shape = MaterialTheme.shapes.extraLarge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
            ) {
                Text(
                    text = stringResource(R.string.city_details_button_search),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}