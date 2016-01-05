package com.lantopia.robocalypse.model

import com.lantopia.robocalypse.model.Function
import com.lantopia.robocalypse.model.Robot
import com.lantopia.robocalypse.model.Direction
import java.util.*
import kotlin.Set;


/**
 * A robotic component, giving the robot a unit of functionality. Most functionality is exposed in the form of new API
 * calls, which the embedded program is able to access.  When a program is bound to a robot, the program will use all
 * of the robot's connected components to build the library that it exposes to the programmer.
 */

public abstract class Component<T : Component<T>>(val name:String?, val functions: Set<Function<T>>)

interface PoweredComponent {
    var load: Int
}

interface MovementComponent {
    var speed: Int
    var direction: Direction
}