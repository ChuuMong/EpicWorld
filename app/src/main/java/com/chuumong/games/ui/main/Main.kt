package com.chuumong.games.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.chuumong.games.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.chuumong.games.domain.model.GameResult
import com.chuumong.games.ui.ScreenState
import com.chuumong.games.ui.common.LoadingItem
import com.chuumong.games.ui.common.SnackBarView
import kotlinx.coroutines.flow.Flow

@Composable
fun MainScreen(
    openSearch: () -> Unit,
    openFilters: () -> Unit,
    openDetail: (Int) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    HandleSideEffect(viewModel.uiSideEffect(), scaffoldState)
    Scaffold(
        topBar = {
            MainAppBar(
                title = stringResource(id = R.string.app_name),
                openSearch = openSearch,
                openFilters = openFilters
            )
        },
        scaffoldState = scaffoldState,
        content = {
            GameListing(
                viewModel = viewModel,
                openDetail = openDetail
            )
        }
    )
}

@Composable
fun MainAppBar(title: String, openSearch: () -> Unit, openFilters: () -> Unit) {
    TopAppBar(
        title = { Text(text = title, color = Color.White) },
        backgroundColor = Color(0xFFF50057),
        actions = {
            IconButton(onClick = openSearch) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search", tint = Color.White)
            }

            IconButton(onClick = openFilters) {
                Icon(imageVector = Icons.Filled.FilterList, contentDescription = "Search", tint = Color.White)
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameListing(viewModel: MainViewModel, openDetail: (Int) -> Unit) {
    val errorMessage = stringResource(id = R.string.home_screen_scroll_error)
    val action = stringResource(id = R.string.all_ok)
    val state = viewModel.uiState().collectAsState()

    when (state.value.screenState) {
        is ScreenState.Loading -> {

        }
        is ScreenState.Error -> {
            ErrorItem { viewModel.initData() }
        }
        is ScreenState.Success -> {
            val gameItems = state.value.games?.collectAsLazyPagingItems() ?: return
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                content = {
                    items(gameItems.itemCount) { index ->
                        gameItems[index]?.let {
                            GameItem(
                                game = it,
                                openDetail = openDetail
                            )
                        }
                    }

                    gameItems.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item { LoadingItem() }
                                item { LoadingItem() }
                            }
                            loadState.append is LoadState.Loading -> {
                                item { LoadingItem() }
                                item { LoadingItem() }
                            }
                            loadState.refresh is LoadState.Error -> {
                                val error = (loadState.refresh as LoadState.Error).error
                                viewModel.handlePaginationDateError(error)
                            }
                            loadState.append is LoadState.Error -> {
                                val error = (loadState.append as LoadState.Error).error
                                viewModel.handlePaginationAppendError(error.message ?: errorMessage, action)
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun GameItem(game: GameResult, openDetail: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(10.dp))
            .height(250.dp)
            .fillMaxWidth()
            .clickable(
                enabled = true,
                onClick = {
                    openDetail(game.id)
                }
            ),
        elevation = 20.dp,
        backgroundColor = Color.Black
    ) {
        ConstraintLayout {
            val (image, title, rating) = createRefs()

            Image(
                modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .height(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
                painter = rememberImagePainter(
                    data = game.backgroundImage,
                    builder = {
                        placeholder(R.drawable.ic_esports_placeholder)
                        crossfade(true)
                    }
                ),
                contentDescription = "Game Image"
            )

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(image.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .padding(8.dp),
                text = game.name,
                color = Color(0xFFF50057),
                maxLines = 2,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Row(modifier = Modifier
                .constrainAs(rating) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
            ) {

                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = game.rating.toString(),
                    color = Color(0xFFFFC400),
                    fontSize = 18.sp
                )

                Image(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    painter = painterResource(id = R.drawable.ic_star),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Game Rading Star"
                )
            }
        }
    }
}

@Composable
fun ErrorItem(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = R.string.home_screen_error_message),
            textAlign = TextAlign.Center,
            color = Black,
            style = com.chuumong.games.ui.theme.Typography.h5
        )
        Button(
            modifier = Modifier.padding(16.dp),
            onClick = onClick
        ) {
            Text(
                text = stringResource(id = R.string.home_screen_retry),
                style = com.chuumong.games.ui.theme.Typography.button
            )
        }
    }
}

@Composable
fun HandleSideEffect(uiSideEffectFlow: Flow<MainSideEffect>, scaffoldState: ScaffoldState) {
    LaunchedEffect(uiSideEffectFlow) {
        val messageHost = SnackBarView(this)
        uiSideEffectFlow.collect { uiSideEffect ->
            when (uiSideEffect) {
                is MainSideEffect.ShowSnackBar -> {
                    messageHost.showSnackBar(
                        snackBarHostState = scaffoldState.snackbarHostState,
                        message = uiSideEffect.message
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainAppBarPreview() {
    MainAppBar(
        title = "Epic World",
        openSearch = {},
        openFilters = {}
    )
}

@Preview(showBackground = true)
@Composable
fun GameItemPreview() {
    GameItem(
        game = GameResult(123, "Max Payne", "", 4.5),
        openDetail = {  }
    )
}

@Preview(showBackground = true)
@Composable
fun ErrorItemPreview() {
    ErrorItem {
        //do nothing
    }
}