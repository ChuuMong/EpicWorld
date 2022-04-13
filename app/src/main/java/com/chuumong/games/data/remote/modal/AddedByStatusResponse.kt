package com.chuumong.games.data.remote.modal

import com.chuumong.games.domain.model.AddedByStatus
import com.google.gson.annotations.SerializedName

data class AddedByStatusResponse(
    @SerializedName("yet")
    val yet: Int,
    @SerializedName("owned")
    val owned: Int,
    @SerializedName("beaten")
    val beaten: Int,
    @SerializedName("toplay")
    val toPlay: Int,
    @SerializedName("dropped")
    val dropped: Int,
    @SerializedName("playing")
    val playing: Int
) {

    fun toEntity() = AddedByStatus(yet, owned, beaten, toPlay, dropped, playing)
}