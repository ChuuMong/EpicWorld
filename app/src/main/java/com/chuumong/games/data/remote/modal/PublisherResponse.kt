package com.chuumong.games.data.remote.modal

import com.chuumong.games.domain.model.Publisher
import com.google.gson.annotations.SerializedName

data class PublisherResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("games_count")
    val games_count: Int,
    @SerializedName("image_background")
    val imageBackground: String
) {

    fun toEntity() = Publisher(id, name, slug, games_count, imageBackground)
}
