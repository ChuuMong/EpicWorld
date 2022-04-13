package com.chuumong.games.ui.detail

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.chuumong.games.R
import com.chuumong.games.domain.model.GameDetail
import com.chuumong.games.domain.model.Genre
import com.chuumong.games.ui.ScreenState
import com.chuumong.games.ui.common.BottomRoundedArcShape
import com.chuumong.games.ui.common.LoadingView
import com.chuumong.games.ui.theme.AmberA400
import com.chuumong.games.ui.theme.Black
import com.chuumong.games.ui.theme.PinkA400
import com.chuumong.games.ui.theme.Typography
import com.chuumong.games.util.extensions.showToast
import kotlinx.coroutines.flow.Flow


@Composable
fun GameDetailScreen(
    id: Int,
    navigateBack: () -> Unit,
    openGameTrailer: (Int) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {

    HandleSideEffect(viewModel.uiSideEffect())

    if (id == 0) {
        viewModel.handleGameIdError()
        navigateBack()
        return
    }

    viewModel.getGameDetail(id)

    val state = viewModel.uiState().collectAsState()
    when (state.value.screenState) {
        is ScreenState.Loading -> {
            LoadingView(modifier = Modifier.fillMaxSize())
        }
        is ScreenState.Error -> {
            Log.e("error", state.value.error?.message, state.value.error)
            navigateBack()
        }
        is ScreenState.Success -> {
            state.value.detail?.let {
                GameDetail(
                    detail = it,
                    openGameTrailer = openGameTrailer
                )
            }
        }
    }
}

enum class DescriptionStatus {
    DEFAULT, SHOW_MORE, SHOW_LESS
}

@Composable
fun GameDetail(detail: GameDetail, openGameTrailer: (Int) -> Unit) {
    val scrollState = rememberScrollState()
    val shouldShowMore = remember {
        mutableStateOf(DescriptionStatus.DEFAULT)
    }

    val maxLines = remember {
        mutableStateOf(4)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .testTag("Game Detail parent")
    ) {
        ConstraintLayout {
            val (play, gameImage) = createRefs()

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .graphicsLayer {
                        clip = true
                        shape = BottomRoundedArcShape()
                    }
                    .constrainAs(gameImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                contentScale = ContentScale.Crop,
                painter = rememberImagePainter(
                    data = detail.backgroundImage,
                    builder = {
                        placeholder(R.drawable.app_logo)
                        crossfade(true)
                    }
                ),
                contentDescription = stringResource(id = R.string.game_details_screenshots)
            )

            PlayTrailer(
                modifier =
                Modifier.constrainAs(play) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                openGameTrailer = { openGameTrailer(detail.id) }
            )
        }

        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 30.dp,
                    end = 16.dp
                ),
            text = detail.name,
            style = Typography.h3,
            color = Black
        )
        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp
                ),
            text = detail.genres.joinToString(" ") { it.name },
            style = Typography.body2,
            color = Black
        )

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (dateLabel, dateValue, ratingLabel, ratingValue) = createRefs()

            Row(modifier = Modifier
                .wrapContentSize()
                .constrainAs(dateLabel) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.game_details_released),
                    style = Typography.h6,
                    color = Black
                )

                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    imageVector = Icons.Filled.Update,
                    contentDescription = stringResource(id = R.string.game_details_calendar_date),
                    tint = AmberA400
                )
            }

            Text(
                modifier = Modifier
                    .constrainAs(dateValue) {
                        top.linkTo(dateLabel.bottom, margin = 8.dp)
                        start.linkTo(dateLabel.start)
                    },
                text = detail.released,
                textAlign = TextAlign.Center,
                color = Black,
                style = Typography.body2
            )

            Row(modifier = Modifier
                .wrapContentSize()
                .constrainAs(ratingLabel) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
            ) {

                Text(
                    text = stringResource(id = R.string.game_details_rating),
                    style = Typography.h6,
                    color = Black
                )

                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    imageVector = Icons.Filled.StarRate,
                    contentDescription = stringResource(id = R.string.all_star_rating),
                    tint = AmberA400
                )
            }

            Text(
                modifier = Modifier
                    .constrainAs(ratingValue) {
                        top.linkTo(ratingLabel.bottom, margin = 8.dp)
                        end.linkTo(ratingLabel.end)
                    },
                text = detail.rating.toString(),
                textAlign = TextAlign.Center,
                color = Black,
                style = Typography.body2
            )
        }

        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp
                ),
            text = stringResource(id = R.string.game_details_about),
            style = Typography.h6,
            color = Black
        )

        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp
                ),
            text = detail.description,
            style = Typography.body2,
            color = Black,
            overflow = TextOverflow.Ellipsis,
            maxLines = maxLines.value,
            onTextLayout = {
                if (it.lineCount == 4 && it.isLineEllipsized(3)) {
                    shouldShowMore.value = DescriptionStatus.SHOW_MORE
                } else if (it.lineCount > 4) {
                    shouldShowMore.value = DescriptionStatus.SHOW_LESS
                }
            }
        )

        when (shouldShowMore.value) {
            DescriptionStatus.SHOW_MORE -> {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .clickable {
                            maxLines.value = Int.MAX_VALUE
                        },
                    text = stringResource(id = R.string.game_details_about_show_more),
                    style = Typography.body2,
                    textDecoration = TextDecoration.Underline,
                    color = PinkA400
                )
            }
            DescriptionStatus.SHOW_LESS -> {
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .clickable {
                            maxLines.value = 4
                        },
                    text = stringResource(id = R.string.game_details_about_show_less),
                    style = Typography.body2,
                    textDecoration = TextDecoration.Underline,
                    color = PinkA400
                )
            }
            else -> Unit
        }

        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp
                ),
            text = stringResource(id = R.string.game_details_platforms),
            style = Typography.h6,
            color = Black
        )
        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp
                ),
            text = detail.platforms.joinToString { it.platform.name },
            style = Typography.body2,
            color = Black
        )
        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp
                ),
            text = stringResource(id = R.string.game_details_stores),
            style = Typography.h6,
            color = Black
        )
        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp
                ),
            text = detail.stores.joinToString { it.store.name },
            style = Typography.body2,
            color = Black
        )
        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp
                ),
            text = stringResource(id = R.string.game_details_developer),
            style = Typography.h6,
            color = Black
        )
        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp
                ),
            text = detail.developers.joinToString { it.name },
            style = Typography.body2,
            color = Black
        )
        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp
                ),
            text = stringResource(id = R.string.game_details_publisher),
            style = Typography.h6,
            color = Black
        )
        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp,
                    bottom = 8.dp
                ),
            text = detail.publishers.joinToString { it.name },
            style = Typography.body2,
            color = Black
        )
    }
}

@Composable
fun PlayTrailer(modifier: Modifier = Modifier, openGameTrailer: () -> Unit) {
    Box(
        modifier = modifier
            .offset(0.dp, 25.dp)
    ) {
        IconButton(onClick = openGameTrailer) {
            Image(
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .align(Alignment.Center)
                    .graphicsLayer {
                        shadowElevation = 20.dp.toPx()
                        shape = RoundedCornerShape(15.dp)
                        clip = true
                    }
                    .background(PinkA400),
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = stringResource(id = R.string.game_details_play_trailer)
            )
        }
    }
}


@Composable
fun HandleSideEffect(sideEffectFlow: Flow<DetailSideEffect>) {
    val context = LocalContext.current
    val gameIdError = stringResource(id = R.string.game_details_invalid_game_id)
    val genericError = stringResource(id = R.string.all_generic_error)

    LaunchedEffect(sideEffectFlow) {
        sideEffectFlow.collect { uiSideEffect ->
            when (uiSideEffect) {
                is DetailSideEffect.ShowGameDetailErrorToast -> {
                    context.showToast(genericError)
                }
                is DetailSideEffect.ShowGameIdErrorToast -> {
                    context.showToast(gameIdError)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameDetailsPreview() {
    GameDetail(
        detail = GameDetail(
            1,
            "Max Payne",
            "The third game in a series, it holds nothing back from the player. Open world adventures of the renowned monster slayer Geralt of Rivia are now even on a larger scale. Following the source material more accurately, this time Geralt is trying to find the child of the prophecy, Ciri while making a quick coin from various contracts on the side",
            4.5,
            "21.03.04",
            "",
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            arrayListOf(),
            listOf(
                Genre(0, "test1", "", 0, ""),
                Genre(0, "test2", "", 0, ""),
                Genre(0, "test3", "", 0, ""),
                Genre(0, "test4", "", 0, ""),
                Genre(0, "test5", "", 0, "")
            ),
            arrayListOf()
        ),
        openGameTrailer = { }
    )
}