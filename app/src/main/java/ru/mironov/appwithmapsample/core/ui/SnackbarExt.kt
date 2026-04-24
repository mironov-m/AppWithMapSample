package ru.mironov.appwithmapsample.core.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberErrorHandler(snackbarHostState: SnackbarHostState): (Throwable) -> Unit {
    val scope = rememberCoroutineScope()
    return remember(snackbarHostState) {
        { throwable -> scope.launch { snackbarHostState.showSnackbar(throwable.message.orEmpty()) } }
    }
}