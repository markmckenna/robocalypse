package com.lantopia.robocalypse.components.movement

import com.lantopia.robocalypse.model.Function
import com.lantopia.robocalypse.model.Robot
import com.lantopia.robocalypse.model.Direction
import com.lantopia.robocalypse.model.World
import java.util.*

/**
 * Created by bsmith on 2015-08-26.
 */

public class SetSpeed: Function<QuadWheels>("SetSpeed", 1){
    override fun invoke(robot: Robot, component: QuadWheels, vararg args: Any?): Any? {
        if (args.size() == 1 && args[0] is Int){
            //TODO: set checks against component abilities
            component.speed = args[0] as Int;
        }else{
            //TODO: Determine what to do when the lua passed arg doesn't match
            //throw IllegalArgumentException("SetSpeed Requires: Int")
        }
        return null;
    }
}

public class GetSpeed: Function<QuadWheels>("GetSpeed", 1){
    override fun invoke(robot: Robot, component: QuadWheels, vararg args: Any?): Any? {    // return the current speed
        return component.speed
    }
}

public class SetOrientation: Function<QuadWheels>("SetOrientation", 1){
    override fun invoke(robot: Robot, component: QuadWheels, vararg args: Any?): Any? {
        if (args.size() == 1 && args[0] is String) {
            // check if what LUA sends us back is a Direction:
            try {
                component.direction = Direction.valueOf(args[0] as String)
            } catch (e: IllegalArgumentException) {
                //TODO: Determine what to do when the lua passed arg doesn't match
            }
        }
        return null
    }
}

public class GetOrientation: Function<QuadWheels>("GetOrientation", 1){
    override fun invoke(robot: Robot, component: QuadWheels, vararg args: Any?): Any? {
        return component.direction
    }
}

public class QuadWheels(override var speed: Int, override var direction: Direction, override var load: Int):
        PoweredComponent,
        MovementComponent,
        Component<QuadWheels>("QUAD_WHEELS",
                setOf(
                        SetSpeed(),
                        GetSpeed(),
                        SetOrientation(),
                        GetOrientation()
                )
        )