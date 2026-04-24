package ru.mironov.appwithmapsample.app.navigation

sealed class Destination(val route: String) {

    data object SearchCity : Destination("search_city")
}