package com.chuumong.games.data.remote.modal

import com.chuumong.games.domain.model.EsrbRating
import com.google.gson.annotations.SerializedName

data class EsrbRatingResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String
) {

    fun toEntity() = EsrbRating(id, name, slug)
}