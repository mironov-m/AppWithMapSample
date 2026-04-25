package ru.mironov.appwithmapsample.feature.map_cities.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.mironov.appwithmapsample.R
import ru.mironov.appwithmapsample.core.android.searchInBrowser
import ru.mironov.appwithmapsample.core.ui.CityInfoFields
import ru.mironov.appwithmapsample.core.ui.theme.AppWithMapSampleTheme
import ru.mironov.appwithmapsample.core.ui.theme.Dimens
import ru.mironov.appwithmapsample.data.cities.models.City

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailsBottomSheet(
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
                    .height(Dimens.ButtonHeight)
                    .padding(top = Dimens.PaddingSmall),
                shape = MaterialTheme.shapes.large,
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

@Preview(showBackground = true)
@Composable
private fun CityDetailsScreenPreview() {
    AppWithMapSampleTheme {
        CityDetailsBottomSheet(
            city = City(
                id = 1,
                name = "Москва",
                country = "Россия",
                lat = 55.75,
                lon = 37.62,
                pop = 12_600_000,
            ),
            onDismiss = {},
        )
    }
}