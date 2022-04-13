package com.chuumong.games.ui.video

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.*
import coil.compose.rememberImagePainter
import com.chuumong.games.R
import com.chuumong.games.domain.model.GameVideo
import com.chuumong.games.domain.model.VideoResult
import com.chuumong.games.ui.ScreenState
import com.chuumong.games.ui.common.LoadingView
import com.chuumong.games.ui.theme.*
import com.chuumong.games.util.extensions.showToast
import com.google.android.exoplayer2.C.TIME_UNSET
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.flow.Flow


@Composable
fun VideosScreen(
    id: Int,
    navigateBack: () -> Unit,
    viewModel: VideoViewModel = hiltViewModel()
) {

    HandleSideEffect(viewModel.uiSideEffect())
    if (id == 0) {
        viewModel.handleIdError()
        navigateBack()
        return
    }

    viewModel.getVideo(id)

    val lifecycleOwner = LocalLifecycleOwner.current
    val stateFlow = viewModel.uiState()
    val stateFlowLifecycleAsare = remember(lifecycleOwner, stateFlow) {
        stateFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val state by stateFlowLifecycleAsare.collectAsState(initial = viewModel.createInitialState())
    when (state.screenState) {
        ScreenState.Loading -> {
            LoadingView(modifier = Modifier.fillMaxSize())
        }
        ScreenState.Error -> {
            Log.e("error", state.error?.message, state.error)
            viewModel.handleVideoError()
            navigateBack.invoke()
        }
        ScreenState.Success -> {
            state.video?.let {
                if (it.count == 0) {
                    viewModel.handleNoVideo()
                    navigateBack.invoke()
                    return
                }

                GameVideo(it)
            }
        }
    }
}

@Composable
private fun GameVideo(video: GameVideo) {
    val playingIndex = remember { mutableStateOf(0) }

    fun onTrailerChange(index: Int) {
        playingIndex.value = index
    }

    Column {
        VideoPlayer(
            modifier = Modifier
                .weight(1f, fill = true),
            videoResults = video.results,
            playingIndex = playingIndex,
            onTrailerChange = { newIndex -> onTrailerChange(newIndex) }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f, fill = true),
            content = {
                itemsIndexed(video.results) { index, trailer ->
                    ShowTrailer(
                        index = index,
                        trailer = trailer,
                        playingIndex = playingIndex,
                        onTrailerClicked = { newIndex -> onTrailerChange(newIndex) }
                    )
                }
            }
        )
    }
}

@Composable
private fun VideoPlayer(
    videoResults: List<VideoResult>,
    playingIndex: State<Int>,
    onTrailerChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isVisible = remember { mutableStateOf(true) }
    val videoTitle = remember { mutableStateOf(videoResults[playingIndex.value].name) }

    val mediaItems = mutableListOf<MediaItem>()
    videoResults.forEach {
        mediaItems.add(
            MediaItem.Builder()
                .setUri(it.video)
                .setMediaId(it.id.toString())
                .setTag(it)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setDisplayTitle(it.name)
                        .build()
                )
                .build()
        )
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItems(mediaItems)
            prepare()
            addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    if (player.contentPosition >= 200) isVisible.value = false
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)

                    onTrailerChange(currentPeriodIndex)
                    isVisible.value = true
                    videoTitle.value = mediaItem?.mediaMetadata?.displayTitle.toString()
                }
            })
        }
    }

    exoPlayer.seekTo(playingIndex.value, TIME_UNSET)
    exoPlayer.playWhenReady = true

    LocalLifecycleOwner.current.lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_START -> {
                    if (exoPlayer.isPlaying.not()) {
                        exoPlayer.play()
                    }
                }
                Lifecycle.Event.ON_STOP -> {
                    exoPlayer.pause()
                }
                else -> Unit
            }
        }
    })

    ConstraintLayout(
        modifier = modifier.background(Black)
    ) {
        val (title, videoPlayer) = createRefs()

        AnimatedVisibility(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            visible = isVisible.value,
            enter = fadeIn(initialAlpha = 0.4f),
            exit = fadeOut(animationSpec = tween(durationMillis = 250))
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                text = videoTitle.value,
                style = Typography.h6,
                color = White
            )
        }

        DisposableEffect(
            AndroidView(
                modifier = modifier
                    .testTag("VideoPlayer")
                    .constrainAs(videoPlayer) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                factory = {
                    StyledPlayerView(context).apply {
                        player = exoPlayer
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                }
            )
        ) {
            onDispose {
                exoPlayer.release()
            }
        }
    }
}

@Composable
private fun ShowTrailer(
    index: Int,
    trailer: VideoResult,
    playingIndex: State<Int>,
    onTrailerClicked: (Int) -> Unit
) {
    val isCurrentlyPlaying = remember { mutableStateOf(false) }
    isCurrentlyPlaying.value = index == playingIndex.value

    ConstraintLayout(
        modifier = Modifier
            .testTag("Trailer Parent")
            .padding(8.dp)
            .wrapContentSize()
            .clickable {
                onTrailerClicked(index)
            }
    ) {
        val (thumbnail, play, title, nowPlaying) = createRefs()

        Image(
            modifier = Modifier
                .height(120.dp)
                .width(120.dp)
                .clip(RoundedCornerShape(20.dp))
                .shadow(elevation = 20.dp)
                .constrainAs(thumbnail) {
                    top.linkTo(parent.top, margin = 8.dp)
                    start.linkTo(parent.start, margin = 8.dp)
                    bottom.linkTo(parent.bottom)
                },
            painter = rememberImagePainter(
                data = trailer.preview,
                builder = {
                    placeholder(R.drawable.app_logo)
                    crossfade(true)
                }
            ),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.game_videos_trailer),
        )

        if (isCurrentlyPlaying.value) {
            Image(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .graphicsLayer {
                        clip = true
                        shadowElevation = 20.dp.toPx()
                    }
                    .constrainAs(play) {
                        top.linkTo(thumbnail.top)
                        start.linkTo(thumbnail.start)
                        end.linkTo(thumbnail.end)
                        bottom.linkTo(thumbnail.bottom)
                    },
                painter = painterResource(id = R.drawable.ic_play),
                colorFilter = if (trailer.preview.isEmpty()) {
                    ColorFilter.tint(White)
                } else {
                    ColorFilter.tint(PinkA400)
                },
                contentDescription = stringResource(id = R.string.game_videos_play)
            )
        }

        Text(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(thumbnail.top, margin = 8.dp)
                    start.linkTo(thumbnail.end, margin = 8.dp)
                    end.linkTo(parent.end, margin = 8.dp)
                    width = Dimension.preferredWrapContent
                    height = Dimension.wrapContent
                },
            text = trailer.name,
            color = Black,
            textAlign = TextAlign.Center,
            softWrap = true,
            style = Typography.h4
        )

        if (isCurrentlyPlaying.value) {
            Text(
                modifier = Modifier
                    .constrainAs(nowPlaying) {
                        top.linkTo(title.bottom, margin = 8.dp)
                        start.linkTo(thumbnail.end, margin = 8.dp)
                        bottom.linkTo(thumbnail.bottom, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                        width = Dimension.preferredWrapContent
                        height = Dimension.preferredWrapContent
                    },
                text = stringResource(id = R.string.game_videos_now_playing),
                color = PinkA400,
                textAlign = TextAlign.Center,
                style = Typography.h6
            )
        }

        TrailerDivider()
    }
}

@Composable
private fun TrailerDivider() {
    Divider(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .testTag("Divider"),
        color = Gray300
    )
}


@Composable
private fun HandleSideEffect(sideEffectFlow: Flow<VideoSideEffect>) {

    val context = LocalContext.current
    val idError = stringResource(id = R.string.game_videos_invalid_game_id)
    val videoError = stringResource(id = R.string.all_generic_error)
    val noVideo = stringResource(id = R.string.game_videos_no_videos)

    LaunchedEffect(sideEffectFlow) {
        sideEffectFlow.collect { uiSideEffect ->
            when (uiSideEffect) {
                is VideoSideEffect.ShowIdErrorToast -> {
                    context.showToast(idError)
                }
                is VideoSideEffect.ShowVideoErrorToast -> {
                    context.showToast(videoError)
                }
                is VideoSideEffect.ShowNoVideoToast -> {
                    context.showToast(noVideo)
                }
            }

        }
    }

}