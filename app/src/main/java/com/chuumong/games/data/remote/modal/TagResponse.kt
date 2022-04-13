package com.chuumong.games.data.remote.modal

import com.chuumong.games.domain.model.Tag
import com.google.gson.annotations.SerializedName

data class TagResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("games_count")
    val gamesCount: Int,
    @SerializedName("image_background")
    val imageBackground: String
) {

    fun toEntity() = Tag(id, name, slug, language, gamesCount, imageBackground)
}