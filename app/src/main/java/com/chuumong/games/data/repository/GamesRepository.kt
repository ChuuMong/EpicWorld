package com.chuumong.games.data.repository

import com.chuumong.games.data.remote.GameApiService
import com.chuumong.games.domain.model.GameDetail
import com.chuumong.games.domain.model.GameVideo
import com.chuumong.games.domain.model.Record
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GamesRepository @Inject constructor(
    private val gameApiService: GameApiService
) {

    suspend fun getAllGames(nextPage: Int) = gameApiService.getAllGames(nextPage, 50)

    suspend fun getGameDetail(id: Int) = gameApiService.getGameDetail(id).toEntity()

    suspend fun getGameVideo(id: Int) = gameApiService.getGameVideo(id).toEntity()
}