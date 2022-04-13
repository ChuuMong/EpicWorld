package com.chuumong.games.domain.model

data class Tag(
    val id : Int,
    val name : String,
    val slug : String,
    val language : String,
    val gamesCount : Int,
    val imageBackground : String
)
