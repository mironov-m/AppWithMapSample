package ru.mironov.appwithmapsample.core.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import ru.mironov.appwithmapsample.R
import ru.mironov.appwithmapsample.core.network.exception.NoInternetException

@Composable
fun rememberErrorHandler(snackbarHostState: SnackbarHostState): (Throwable) -> Unit {
    val scope = rememberCoroutineScope()
    val noInternetMessage = stringResource(R.string.error_no_internet)
    val unknownErrorMessage = stringResource(R.string.error_unknown)
    return remember(snackbarHostState) {
        { throwable ->
            val message = when (throwable) {
                is NoInternetException -> noInternetMessage
                else -> unknownErrorMessage
            }
            scope.launch { snackbarHostState.showSnackbar(message) }
        }
    }
}