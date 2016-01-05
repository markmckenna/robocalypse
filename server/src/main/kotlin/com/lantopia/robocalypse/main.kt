package com.lantopia.robocalypse

import com.lantopia.robocalypse.model.MovementComponent
import com.lantopia.robocalypse.model.PoweredComponent
import com.lantopia.robocalypse.model.Game


/** Executes the main game loop for the Robocalypse game. */
fun main(args: Array<String>) {

}


/**
 * Executed every time the game is allowed to update.  This function is responsible for executing the
 * programs of each robot in the game and updating context events.
 */
fun Game.update(tick: Int) {
    // Have each robot run its program
    world.robots.forEach {
        val (robot, pos) = it
        robot.runCycle(tick) // TODO: Apply some kind of rlimit here
    }

    world.robots.forEach {
        val (robot, pos) = it
        // move robots with power and speed

    }
}
