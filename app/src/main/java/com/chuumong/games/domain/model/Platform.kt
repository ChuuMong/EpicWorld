package com.chuumong.games.domain.model

data class ParentPlatform(
    val platform: PlatformInfo
)

data class Platform(
    val platform: PlatformInfo,
    val releasedAt: String?,
    val requirementsEn: Requirement?,
    val requirementsRu: Requirement?
)

data class PlatformInfo(
    val id: Int,
    val name: String,
    val slug: String,
    val image: String?,
    val yearEnd: String?,
    val yearStart: String?,
    val gamesCount: Int,
    val imageBackground: String?
)