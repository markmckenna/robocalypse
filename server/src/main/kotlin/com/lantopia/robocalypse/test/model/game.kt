package com.lantopia.robocalypse.test.model

public data class Player(val name : String, val robots : Set<Robot>)

public data class Game(val world : World, val players : Set<Player>)
