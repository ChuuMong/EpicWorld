package com.chuumong.games.data.remote.modal

import com.chuumong.games.domain.model.ParentPlatform
import com.chuumong.games.domain.model.Platform
import com.chuumong.games.domain.model.PlatformInfo
import com.chuumong.games.domain.model.Requirement
import com.google.gson.annotations.SerializedName

data class PlatformResponse(
    @SerializedName("platform")
    val platform: PlatformInfoResponse,
    @SerializedName("released_at")
    val releasedAt: String?,
    @SerializedName("requirements_en")
    val requirementsEn: RequirementResponse?,
    @SerializedName("requirements_ru")
    val requirementsRu: RequirementResponse?
) {

    fun toEntity() = Platform(
        platform.toEntity(),
        releasedAt,
        requirementsEn?.toEntity(),
        requirementsRu?.toEntity()
    )
}

data class ParentPlatformResponse(
    @SerializedName("platform")
    val platform: PlatformInfoResponse
) {

    fun toEntity() = ParentPlatform(
        platform.toEntity()
    )
}

data class PlatformInfoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("image")
    val image: String?,
    @SerializedName("year_end")
    val yearEnd: String?,
    @SerializedName("year_start")
    val yearStart: String?,
    @SerializedName("games_count")
    val gamesCount: Int,
    @SerializedName("image_background")
    val imageBackground: String?
) {

    fun toEntity() = PlatformInfo(
        id, name, slug, image, yearEnd, yearStart, gamesCount, imageBackground
    )
}

data class RequirementResponse(
    @SerializedName("minimum")
    val minimum: String?,
    @SerializedName("recommended")
    val recommended: String?
) {

    fun toEntity() = Requirement(minimum, recommended)
}