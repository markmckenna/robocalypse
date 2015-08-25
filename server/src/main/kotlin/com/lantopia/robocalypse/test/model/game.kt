package com.lantopia.robocalypse.model

public data class Player(val name : String, val robots : Set<Robot>)

public data class Game(val world : World, val players : Set<Player>)
