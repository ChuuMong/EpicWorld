package com.chuumong.games.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chuumong.games.ui.detail.GameDetailScreen
import com.chuumong.games.ui.main.GamesDestinations.Detail
import com.chuumong.games.ui.main.GamesDestinations.Filter
import com.chuumong.games.ui.main.GamesDestinations.Main
import com.chuumong.games.ui.main.GamesDestinations.Search
import com.chuumong.games.ui.main.GamesDestinations.Video
import com.chuumong.games.ui.theme.GamesTheme
import com.chuumong.games.ui.video.VideosScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamesTheme {
                MainView()
            }
        }
    }
}

@Composable
fun MainView() {
    val navController = rememberNavController()
    val action = remember(navController) { GameActions(navController) }

    NavHost(navController = navController, startDestination = Main) {
        composable(Main) {
            MainScreen(
                openSearch = action.openSearch,
                openFilters = action.openFilter,
                openDetail = action.openDetail
            )
        }

        composable(
            "$Detail/{${GamesDestinations.DetailArgs.Id}}",
            arguments = listOf(
                navArgument(GamesDestinations.DetailArgs.Id) { type = NavType.IntType }
            )
        ) {
            GameDetailScreen(
                id = it.arguments?.getInt(GamesDestinations.DetailArgs.Id) ?: 0,
                navigateBack = action.navigateBack,
                openGameTrailer = action.openGameVideo
            )
        }

        composable(
            "$Video/{${GamesDestinations.VideoArgs.Id}}",
            arguments = listOf(
                navArgument(GamesDestinations.VideoArgs.Id) { type = NavType.IntType }
            )
        ) {
            VideosScreen(
                id = it.arguments?.getInt(GamesDestinations.VideoArgs.Id) ?: 0,
                navigateBack = action.navigateBack
            )
        }
    }
}

object GamesDestinations {
    const val Main = "Main"
    const val Detail = "Detail"
    const val Video = "Video"
    const val Search = "Search"
    const val Filter = "Filter"

    object DetailArgs {
        const val Id = "Id"
    }

    object VideoArgs {
        const val Id = "Id"
    }

}


class GameActions(navHostController: NavHostController) {
    val openSearch: () -> Unit = {
        navHostController.navigate(Search)
    }

    val openFilter: () -> Unit = {
        navHostController.navigate(Filter)
    }

    val openDetail: (Int) -> Unit = { id ->
        navHostController.navigate("$Detail/$id")
    }

    val openGameVideo: (Int) -> Unit = { id ->
        navHostController.navigate("$Video/$id")
    }

    val navigateBack: () -> Unit = {
        navHostController.navigateUp()
    }
}