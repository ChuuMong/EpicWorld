package com.chuumong.games.domain.usecase

import com.chuumong.games.data.repository.GamesRepository
import com.chuumong.games.domain.model.GameVideo
import com.chuumong.games.domain.model.Record
import javax.inject.Inject


class GetGameVideo @Inject constructor(
    private val gamesRepository: GamesRepository
) : BaseUseCase<Int, Record<GameVideo>>() {

    override suspend fun run(request: Int): Record<GameVideo> {
        return try {
            Record(gamesRepository.getGameVideo(request), null)
        } catch (e: Exception) {
            Record(null, e)
        }
    }
}