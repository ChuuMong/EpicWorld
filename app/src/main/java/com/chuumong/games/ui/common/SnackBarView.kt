package com.chuumong.games.ui.common

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class SnackBarView(private val scope: CoroutineScope) {

    private var snackBarJob: Job? = null

    fun showSnackBar(snackBarHostState: SnackbarHostState, message: String) {
        snackBarJob?.cancel()

        snackBarJob = scope.launch {
            snackBarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
        }
    }



}