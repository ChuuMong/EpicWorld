package com.chuumong.games.data.remote.modal

import com.chuumong.games.domain.model.Store
import com.chuumong.games.domain.model.StoreInfo
import com.google.gson.annotations.SerializedName


data class StoreResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("store")
    val store: StoreInfoResponse
) {

    fun toEntity() = Store(id, store.toEntity())
}

data class StoreInfoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("domain")
    val domain: String,
    @SerializedName("games_count")
    val gamesCount: Int,
    @SerializedName("image_background")
    val imageBackground: String
) {

    fun toEntity() = StoreInfo(id, name, slug, domain, gamesCount, imageBackground)
}