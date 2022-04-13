package com.chuumong.games.data.remote.modal

import com.chuumong.games.domain.model.Genre
import com.google.gson.annotations.SerializedName


data class GenreResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("games_count")
    val gamesCount: Int,
    @SerializedName("image_background")
    val imageBackground: String
) {

    fun toEntity() = Genre(id, name, slug, gamesCount, imageBackground)
}