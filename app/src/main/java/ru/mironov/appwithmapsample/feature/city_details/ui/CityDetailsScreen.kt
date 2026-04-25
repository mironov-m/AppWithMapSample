package ru.mironov.appwithmapsample.feature.city_details.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.mironov.appwithmapsample.R
import ru.mironov.appwithmapsample.core.android.searchInBrowser
import ru.mironov.appwithmapsample.core.ui.CityInfoFields
import ru.mironov.appwithmapsample.core.ui.theme.AppWithMapSampleTheme
import ru.mironov.appwithmapsample.core.ui.theme.Dimens
import ru.mironov.appwithmapsample.data.cities.models.City

@Composable
fun CityDetailsScreen(
    city: City,
    navController: NavHostController,
) {
    val context = LocalContext.current
    CityDetailsContent(
        city = city,
        onBackClick = { navController.popBackStack() },
        onSearchClick = { searchInBrowser(context, city.name) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CityDetailsContent(
    city: City,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.city_details_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.city_details_back_desc)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CityInfoFields(
                city = city,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.PaddingMedium)
                    .padding(top = Dimens.PaddingMedium),
            )

            Button(
                onClick = onSearchClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(Dimens.ButtonHeight)
                    .padding(horizontal = Dimens.PaddingMedium)
                    .padding(bottom = Dimens.PaddingLarge),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = stringResource(R.string.city_details_button_search),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CityDetailsScreenPreview() {
    AppWithMapSampleTheme {
        CityDetailsContent(
            city = City(
                id = 1,
                name = "Москва",
                country = "Россия",
                lat = 55.75,
                lon = 37.62,
                pop = 12_600_000,
            ),
            onBackClick = {},
            onSearchClick = {},
        )
    }
}