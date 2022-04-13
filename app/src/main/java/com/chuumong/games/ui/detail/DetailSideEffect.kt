package com.chuumong.games.ui.detail

sealed class DetailSideEffect {

    object ShowGameIdErrorToast: DetailSideEffect()

    object ShowGameDetailErrorToast: DetailSideEffect()
}
