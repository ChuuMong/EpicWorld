package com.chuumong.games.data.remote

import com.chuumong.games.data.remote.modal.GamesResponse
import com.chuumong.games.data.remote.modal.GameDetailsResponse
import com.chuumong.games.data.remote.modal.GameVideoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GameApiService {

    @GET("/api/games")
    suspend fun getAllGames(@Query("page") page: Int, @Query("page_size") pageSize: Int): GamesResponse

    @GET("/api/games/{id}")
    suspend fun getGameDetail(@Path("id") gameId: Int): GameDetailsResponse

    @GET("api/games/{id}/movies")
    suspend fun getGameVideo(@Path("id") gameId: Int): GameVideoResponse
}