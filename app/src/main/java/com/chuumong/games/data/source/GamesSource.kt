package com.chuumong.games.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.chuumong.games.domain.model.GameResult
import com.chuumong.games.data.repository.GamesRepository
import javax.inject.Inject


class GamesSource @Inject constructor(
    private val gamesRepository: GamesRepository
) : PagingSource<Int, GameResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GameResult> {
        val nextPage = params.key ?: 1

        return try {
            val gameResponse = gamesRepository.getAllGames(nextPage)
            LoadResult.Page(
                data = gameResponse.toGameResultEntities(),
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GameResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}