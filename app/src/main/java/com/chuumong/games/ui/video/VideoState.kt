package com.chuumong.games.ui.video

import com.chuumong.games.domain.model.GameVideo
import com.chuumong.games.ui.ScreenState


data class VideoState(
    val screenState: ScreenState,
    val video: GameVideo?,
    val error: Throwable?
)