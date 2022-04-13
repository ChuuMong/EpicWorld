package com.chuumong.games.ui.video

import androidx.lifecycle.viewModelScope
import com.chuumong.games.domain.usecase.GetGameVideo
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
class VideoViewModel @Inject constructor(
    private val getGameVideo: GetGameVideo
) : BaseViewModel<VideoState, VideoSideEffect>() {

    override fun createInitialState() = VideoState(ScreenState.Loading, null, null)

    fun getVideo(id: Int) = intent {
        getGameVideo.invoke(
            viewModelScope,
            Dispatchers.IO,
            id
        ) { recode ->
            viewModelScope.launch {
                reduce {
                    if (recode?.data != null) {
                        state.copy(
                            screenState = ScreenState.Success,
                            video = recode.data,
                            error = null
                        )
                    } else {
                        state.copy(
                            screenState = ScreenState.Error,
                            video = null,
                            error = recode?.error
                        )
                    }
                }
            }
        }
    }

    fun handleIdError() = intent {
        postSideEffect(VideoSideEffect.ShowIdErrorToast)
    }

    fun handleVideoError() = intent {
        postSideEffect(VideoSideEffect.ShowVideoErrorToast)
    }

    fun handleNoVideo() = intent {
        postSideEffect(VideoSideEffect.ShowNoVideoToast)
    }
}