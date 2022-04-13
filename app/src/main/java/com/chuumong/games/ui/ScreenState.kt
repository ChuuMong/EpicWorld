package com.chuumong.games.ui


sealed class ScreenState {
    object Loading: ScreenState()
    object Success: ScreenState()
    object Error: ScreenState()
}
