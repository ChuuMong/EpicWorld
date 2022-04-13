package com.chuumong.games.data.remote.modal

import com.chuumong.games.domain.model.Rating
import com.google.gson.annotations.SerializedName


data class RatingResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("count")
    val count: Int,
    @SerializedName("percent")
    val percent: Double
) {

    fun toEntity() = Rating(
        id, title, count, percent
    )
}