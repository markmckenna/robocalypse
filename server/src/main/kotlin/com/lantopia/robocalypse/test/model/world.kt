package com.lantopia.robocalypse.test.model

public data class Position(val x : Int, val y : Int)
public data class Bunker(val player: Player)

/**
 * Represents a world in which a game takes place.
 * @param name indicates the name of the world, used to generate save files.
 * @param lastPosition indicates the largest x/y coordinate that is valid; 0/0 is always the lowest.
 * @param robots indicates what robots are in the map, and where they are.
 * @param components indicates what components are in the map, and where they are.
 * @param bunkers indicates what players are in the map, and where their bunkers are.
 */
public data class World(val name : String,
            val lastPosition : Position,
            val robots : MutableMap<ProgrammedRobot, Position>,
            val components : MutableMap<Component, Position>,
            val bunkers : Map<Bunker, Position>)
