package ru.mironov.appwithmapsample.core.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import ru.mironov.appwithmapsample.core.network.exception.NoInternetException

@Composable
fun rememberErrorHandler(snackbarHostState: SnackbarHostState): (Throwable) -> Unit {
    val scope = rememberCoroutineScope()
    return remember(snackbarHostState) {
        { throwable ->
            val message = when (throwable) {
                is NoInternetException -> "Нет подключения к интернету"
                else -> "Что-то пошло не так, попробуйте еще раз"
            }
            scope.launch { snackbarHostState.showSnackbar(message) }
        }
    }
}