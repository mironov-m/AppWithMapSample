package ru.mironov.appwithmapsample.feature.map_cities.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

val singapore = LatLng(1.35, 103.87)

@Composable
fun MapCitiesScreen() {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
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
                Marker(
                    state = MarkerState(position = singapore),
                    title = "Singapore",
                    snippet = "Test location"
                )
            }
        }
    }
}