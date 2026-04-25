package ru.mironov.appwithmapsample.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import ru.mironov.appwithmapsample.R
import ru.mironov.appwithmapsample.core.ui.theme.Dimens
import ru.mironov.appwithmapsample.core.utils.formatCountryName
import ru.mironov.appwithmapsample.data.cities.models.City
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CityInfoFields(
    city: City,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium),
    ) {
        LabeledField(
            label = stringResource(R.string.city_details_label_city),
            value = city.name,
        )
        LabeledField(
            label = stringResource(R.string.city_details_label_country),
            value = formatCountryName(city.country),
        )
        LabeledField(
            label = stringResource(R.string.city_details_label_population),
            value = stringResource(R.string.city_details_population_format, city.pop.formatNumber()),
        )
    }
}

@Composable
private fun LabeledField(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall / 2)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

private fun Long.formatNumber(): String {
    return NumberFormat.getNumberInstance(Locale.forLanguageTag("ru"))
        .format(this)
}