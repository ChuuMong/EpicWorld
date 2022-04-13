package com.chuumong.games.data.remote.modal

import com.chuumong.games.domain.model.ShortScreenshot
import com.google.gson.annotations.SerializedName

data class ShortScreenshotResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String
) {
    fun toEntity() = ShortScreenshot(id, image)
}
