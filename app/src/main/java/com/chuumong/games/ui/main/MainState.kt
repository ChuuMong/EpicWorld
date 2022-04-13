package com.chuumong.games.ui.main

import androidx.paging.PagingData
import com.chuumong.games.domain.model.GameResult
import com.chuumong.games.ui.ScreenState
import kotlinx.coroutines.flow.Flow


data class MainState(
    val screenState: ScreenState,
    val games: Flow<PagingData<GameResult>>?,
    val error: Throwable?
)