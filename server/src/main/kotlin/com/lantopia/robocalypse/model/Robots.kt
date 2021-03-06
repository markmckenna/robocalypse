package com.lantopia.robocalypse.model

import kotlin.reflect.KCallable
import kotlin.reflect.KClass

/**
 * A robotic component, giving the robot a unit of functionality. Most functionality is exposed in the form of new API
 * calls, which the embedded program is able to access.  When a program is bound to a robot, the program will use all
 * of the robot's connected components to build the library that it exposes to the programmer.
 */
public abstract data class Component(val name: String, val functions: Set<Function>)

/**
 * A robot that does its master's bidding (in theory).
 *
 * The robot is made up of components, which add capabilities to the robot as a whole.  When a robot is created, its
 * listed components are searched for API functions, which are added to the library of functions that are available to
 * the robot's control code.  These components may also impact some 'meta properties' of the robot, such as how many
 * cycles are available to the robot in each tick, how much general-purpose data storage is available, how resistant
 * the robot is to the actions of other robots and the environment, and so on.
 */
public data class Robot(val owner: Player, val components: Set<Component>)


/** One function exposed by a component */
public abstract class Function(val name: String, val args: Int) {
    /** Execute this function to... execute this function */
    public abstract fun invoke(robot: Robot, vararg args: Any?): Any?
}


/** The program that a given robot executes. */
public abstract class ProgrammedRobot(val script: String, val robot: Robot) {
    /** Execute this program on the given robot at the given time. Only data available through the robot
     * and components within that robot is available for use by the program. */
    public abstract fun runCycle(tick: Int)
}
