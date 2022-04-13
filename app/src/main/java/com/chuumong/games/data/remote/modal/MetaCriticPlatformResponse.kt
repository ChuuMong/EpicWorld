package com.chuumong.games.data.remote.modal

import com.google.gson.annotations.SerializedName

data class MetaCriticPlatformResponse(
    @SerializedName("metascore")
    val metaScore : Int,
    @SerializedName("url")
    val url : String,
    @SerializedName("platform")
    val platform : PlatformInfoResponse
)
