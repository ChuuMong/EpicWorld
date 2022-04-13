package com.chuumong.games.domain.model

data class GameDetail(
    val id: Int,
    val name: String,
    val description: String,
    val rating: Double,
    val released: String,
    val backgroundImage: String,
    val parentPlatforms: List<ParentPlatform>,
    val platforms: List<Platform>,
    val stores: List<Store>,
    val developers: List<Developer>,
    val genres: List<Genre>,
    val publishers: List<Publisher>
)
