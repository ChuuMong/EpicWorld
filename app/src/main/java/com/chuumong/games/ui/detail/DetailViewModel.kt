package com.chuumong.games.ui.detail

import androidx.lifecycle.viewModelScope
import com.chuumong.games.data.remote.modal.GetGameDetailRequest
import com.chuumong.games.domain.usecase.GetGameDetail
import com.chuumong.games.ui.BaseViewModel
import com.chuumong.games.ui.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getGameDetail: GetGameDetail
) : BaseViewModel<DetailState, DetailSideEffect>() {

    override fun createInitialState() = DetailState(ScreenState.Loading, null, null)

    fun getGameDetail(id: Int) = intent {
        getGameDetail.invoke(
            viewModelScope,
            Dispatchers.IO,
            GetGameDetailRequest(id)
        ) { record ->
            viewModelScope.launch {
                reduce {
                    if (record?.data != null) {
                        state.copy(
                            screenState = ScreenState.Success,
                            detail = record.data,
                            error = null
                        )
                    } else {
                        handleError()
                        state.copy(
                            screenState = ScreenState.Error,
                            detail = null,
                            error = record?.error
                        )
                    }
                }
            }
        }
    }

    fun handleGameIdError() = intent {
        postSideEffect(DetailSideEffect.ShowGameIdErrorToast)
    }

    private fun handleError() = intent {
        postSideEffect(DetailSideEffect.ShowGameDetailErrorToast)
    }

}