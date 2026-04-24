package ru.mironov.appwithmapsample.feature.search_city.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.mironov.appwithmapsample.feature.search_city.presentation.SearchCitySideEffect
import ru.mironov.appwithmapsample.feature.search_city.presentation.SearchCityViewModel

@Composable
fun SearchCityScreen(
    onNavigateToCity: (Long) -> Unit = {},
    viewModel: SearchCityViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is SearchCitySideEffect.NavigateToCity -> onNavigateToCity(effect.cityId)
            is SearchCitySideEffect.ShowError -> Unit // TODO: show snackbar
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Search City Screen")
    }
}
