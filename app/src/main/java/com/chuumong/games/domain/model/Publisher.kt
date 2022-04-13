package com.chuumong.games.domain.model

data class Publisher(
    val id: Int,
    val name: String,
    val slug: String,
    val games_count: Int,
    val imageBackground: String
)
