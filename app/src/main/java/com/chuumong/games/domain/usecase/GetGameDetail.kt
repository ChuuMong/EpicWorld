package com.chuumong.games.domain.usecase

import com.chuumong.games.data.remote.modal.GetGameDetailRequest
import com.chuumong.games.data.repository.GamesRepository
import com.chuumong.games.domain.model.GameDetail
import com.chuumong.games.domain.model.Record
import javax.inject.Inject


class GetGameDetail @Inject constructor(
    private val gamesRepository: GamesRepository
) : BaseUseCase<GetGameDetailRequest, Record<GameDetail>>() {

    override suspend fun run(request: GetGameDetailRequest): Record<GameDetail> {
        return try {
            Record(gamesRepository.getGameDetail(request.gameId), null)
        } catch (e: Exception) {
            Record(null, e)
        }
    }
}