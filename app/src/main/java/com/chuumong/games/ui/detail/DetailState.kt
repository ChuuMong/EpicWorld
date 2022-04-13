package com.chuumong.games.ui.detail

import com.chuumong.games.domain.model.GameDetail
import com.chuumong.games.ui.ScreenState

data class DetailState(
    val screenState: ScreenState,
    val detail: GameDetail?,
    val error: Throwable?
)
