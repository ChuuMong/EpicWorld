package com.chuumong.games.ui.main

sealed class MainSideEffect {

    data class ShowSnackBar(val message: String, val action: String): MainSideEffect()
}
