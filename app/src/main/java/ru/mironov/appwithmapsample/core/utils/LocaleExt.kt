package ru.mironov.appwithmapsample.core.utils

import java.util.Locale

fun formatCountryName(code: String): String {
    return Locale("", code)
        .getDisplayCountry(Locale.forLanguageTag("ru"))
}