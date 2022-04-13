package com.chuumong.games.ui.video


sealed class VideoSideEffect {
    object ShowIdErrorToast: VideoSideEffect()
    object ShowVideoErrorToast: VideoSideEffect()
    object ShowNoVideoToast: VideoSideEffect()
}