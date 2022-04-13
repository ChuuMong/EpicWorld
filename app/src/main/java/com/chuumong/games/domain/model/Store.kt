package com.chuumong.games.domain.model

data class Store(
    val id : Int,
    val store : StoreInfo
)

data class StoreInfo(
    val id : Int,
    val name : String,
    val slug : String,
    val domain : String,
    val gamesCount : Int,
    val imageBackground : String
)
