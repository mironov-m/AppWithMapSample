package ru.mironov.appwithmapsample.feature.search_city.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.mironov.appwithmapsample.core.ui.theme.AppWithMapSampleTheme
import ru.mironov.appwithmapsample.core.ui.theme.Dimens
import ru.mironov.appwithmapsample.core.utils.resource.Resource
import ru.mironov.appwithmapsample.feature.search_city.data.models.City
import ru.mironov.appwithmapsample.feature.search_city.data.models.CitiesResponse
import ru.mironov.appwithmapsample.feature.search_city.presentation.SearchCitySideEffect
import ru.mironov.appwithmapsample.feature.search_city.presentation.SearchCityState
import ru.mironov.appwithmapsample.feature.search_city.presentation.SearchCityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCityScreen(
    onNavigateToCity: (Long) -> Unit = {},
    viewModel: SearchCityViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is SearchCitySideEffect.NavigateToCity -> onNavigateToCity(effect.cityId)
            is SearchCitySideEffect.ShowError -> coroutineScope.launch {
                snackbarHostState.showSnackbar(effect.error.message.orEmpty()) // TODO
            }
        }
    }

    SearchCityContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onQueryChanged = viewModel::onQueryChanged,
        onClearQuery = viewModel::onClearQuery,
        onCitySelected = viewModel::onCitySelected,
        onLoadMore = viewModel::onLoadMore,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchCityContent(
    state: SearchCityState,
    snackbarHostState: SnackbarHostState,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () -> Unit,
    onCitySelected: (Long) -> Unit,
    onLoadMore: () -> Unit,
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisible >= totalItems - Dimens.PaginationThreshold
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && state.cities.isNotEmpty()) {
            onLoadMore()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Список городов",
                    style = MaterialTheme.typography.titleLarge,
                )
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Dimens.PaddingMedium, vertical = Dimens.PaddingSmall)
        ) {
            SearchBar(
                query = state.query,
                onQueryChanged = onQueryChanged,
                onClearQuery = onClearQuery,
            )

            when {
                state.cities.isNotEmpty() -> {
                    CityList(
                        cities = state.cities,
                        isLoading = state.citiesResponse is Resource.Loading,
                        listState = listState,
                        onCitySelected = onCitySelected,
                    )
                }

                state.citiesResponse is Resource.Loading -> {
                    FullScreenLoading()
                }

                state.citiesResponse is Resource.Success -> {
                    EmptySearchResult()
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () -> Unit,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Введите название города",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearQuery) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Очистить",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
}

@Composable
private fun CityListItem(
    city: City,
    onCitySelected: (Long) -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = city.name,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        supportingContent = {
            Text(
                text = city.country,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        leadingContent = {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        modifier = Modifier.clickable {
            onCitySelected(city.id)
        }
    )
    HorizontalDivider()
}

@Composable
private fun CityList(
    cities: List<City>,
    isLoading: Boolean,
    listState: LazyListState,
    onCitySelected: (Long) -> Unit,
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimens.PaddingSmall)
    ) {
        items(cities, key = { it.id }) { city ->
            CityListItem(
                city = city,
                onCitySelected = onCitySelected,
            )
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.PaddingMedium),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun EmptySearchResult() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Ничего не найдено",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchCityScreenPreview() {
    AppWithMapSampleTheme {
        SearchCityContent(
            state = SearchCityState(),
            snackbarHostState = SnackbarHostState(),
            onQueryChanged = {},
            onClearQuery = {},
            onCitySelected = {},
            onLoadMore = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchCityScreenWithResultsPreview() {
    AppWithMapSampleTheme {
        val sampleCities = listOf(
            City(id = 1, name = "Москва", country = "Россия", lat = 55.75, lon = 37.62, pop = 12_600_000),
            City(id = 2, name = "Санкт-Петербург", country = "Россия", lat = 59.93, lon = 30.32, pop = 5_400_000),
            City(id = 3, name = "Новосибирск", country = "Россия", lat = 55.03, lon = 82.92, pop = 1_600_000),
        )
        SearchCityContent(
            state = SearchCityState(
                query = "Москва",
                citiesResponse = Resource.Success(
                    CitiesResponse(items = sampleCities, limit = 10, page = 1, total = 3)
                ),
                cities = sampleCities,
            ),
            snackbarHostState = SnackbarHostState(),
            onQueryChanged = {},
            onClearQuery = {},
            onCitySelected = {},
            onLoadMore = {},
        )
    }
}