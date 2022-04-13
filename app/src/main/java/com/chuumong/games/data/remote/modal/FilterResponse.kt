package com.chuumong.games.data.remote.modal

import com.google.gson.annotations.SerializedName

data class FilterResponse(
    @SerializedName("years")
    val years: List<YearResponse>
)


data class YearResponse(
    @SerializedName("from")
    val from: Int,
    @SerializedName("to")
    val to: Int,
    @SerializedName("filter")
    val filter: String,
    @SerializedName("decade")
    val decade: Int,
    @SerializedName("years") val years: List<YearInfoResponse>,
)

data class YearInfoResponse(
    @SerializedName("year")
    val year: Int,
    @SerializedName("nofollow")
    val noFollow: Boolean,
    @SerializedName("count")
    val count: Int
)