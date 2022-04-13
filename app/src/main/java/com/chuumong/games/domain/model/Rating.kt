package com.chuumong.games.domain.model

data class Rating(
    val id : Int,
    val title : String,
    val count : Int,
    val percent : Double
)
