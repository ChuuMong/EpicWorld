package com.chuumong.games.ui.main

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.chuumong.games.data.source.GamesSource
import com.chuumong.games.ui.BaseViewModel
import com.chuumong.games.ui.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val gamesSource: GamesSource
) : BaseViewModel<MainState, MainSideEffect>() {

    override fun createInitialState(): MainState = MainState(ScreenState.Loading, null, null)

    override fun initData() = intent {
        val result = getAllGames()
        reduce {
            state.copy(
                screenState = ScreenState.Success,
                games = result.flow,
                error = null
            )
        }
    }

    fun handlePaginationDateError(throwable: Throwable?) = intent {
        reduce {
            state.copy(
                screenState = ScreenState.Error,
                games = null,
                error = throwable
            )
        }
    }

    fun handlePaginationAppendError(message: String, action: String) = intent {
        postSideEffect(MainSideEffect.ShowSnackBar(message, action))
    }

    private fun getAllGames() = Pager(PagingConfig(50)) { gamesSource }
}