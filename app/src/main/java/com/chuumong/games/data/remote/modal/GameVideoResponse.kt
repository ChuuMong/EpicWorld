package com.chuumong.games.data.remote.modal

import com.chuumong.games.domain.model.GameVideo
import com.chuumong.games.domain.model.VideoResult
import com.google.gson.annotations.SerializedName

data class GameVideoResponse(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String,
    @SerializedName("results")
    val results: List<VideoResultResponse>
) {

    fun toEntity() = GameVideo(count = count, results = results.map { it.toEntity() })
}

data class VideoResultResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("preview")
    val preview: String,
    @SerializedName("data")
    val data: VideoDataResponse
) {

    fun toEntity() = VideoResult(preview = preview, name = name, video = data.max, id = id)
}

data class VideoDataResponse(
    @SerializedName("480")
    val medium: String,
    @SerializedName("max")
    val max: String
)