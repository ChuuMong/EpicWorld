package com.chuumong.games.domain.model

data class Record<out R>(
    val data: R?,
    val error: Throwable?
)
