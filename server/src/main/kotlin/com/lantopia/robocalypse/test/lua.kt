package com.lantopia.robocalypse

import com.lantopia.robocalypse.model.ProgrammedRobot
import com.lantopia.robocalypse.model.Robot
import org.luaj.vm2.*
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.VarArgFunction
import org.luaj.vm2.lib.jse.JsePlatform

class LuaProgrammedRobot(script: String, robot: Robot) : ProgrammedRobot(script, robot) {
    val compiledScript: LuaValue

    init {
        val globals = JsePlatform.standardGlobals() // TODO: Prune

        robot.components.forEach { component ->
            globals.get("require").call(
                LuaValue.valueOf(component.name),
                object : TwoArgFunction() {
                    override fun call(moduleName: LuaValue, env: LuaValue): LuaValue? {
                        val library = LuaValue.tableOf(
                            component.functions.flatMap { function ->
                                listOf(LuaValue.valueOf(function.name),
                                        object : VarArgFunction() {
                                            override fun invoke(args: Varargs?) : Varargs? {
                                                when (args) {
                                                    null -> return toLuaValue(function.invoke(robot))
                                                    else -> return toLuaValue(function.invoke(robot,
                                                                0.rangeTo(args.narg()).map { i ->
                                                                    fromLuaValue(args.arg(i))
                                                                }))
                                                }
                                            }
                                        })
                            }.toTypedArray()
                        )
                        env.set(moduleName, library)
                        return library
                    }
                })
        }

        compiledScript = globals.load(script)
    }

    override fun runCycle(tick: Int) {
        // Globals exposes the Lua standard library, which we would like to enhance by adding routines exposed by
        // robot components.

    }
}

/** Convert a (nullable) Lua value into its appropriate (nullable) Kotlin type equivalent */
fun fromLuaValue(value: LuaValue?): Any? {
    return when (value) {
        null -> null
        LuaValue.NIL -> null
        is LuaBoolean -> value.booleanValue()
        is LuaInteger -> value.tolong()
        is LuaDouble -> value.todouble()
        is LuaString -> value.tojstring()
        is LuaTable -> mapOf(*value.keys().map { key ->
            Pair(fromLuaValue(key), fromLuaValue(value.get(key)))
        }.toTypedArray())
        is LuaThread, is LuaFunction
        -> throw UnsupportedOperationException("thread, function not yet supported")
        else -> value.touserdata()
    }
}

/** Convert any normal Java/Kotlin type, or null, to a reasonable LuaValue instance. */
fun toLuaValue(value: Any?): LuaValue? {
    return when (value) {
        null -> LuaValue.NIL // TODO: Is this right?
        is Boolean -> LuaValue.valueOf(value)
        is Short -> LuaValue.valueOf(value.toInt())
        is Int -> LuaValue.valueOf(value)
        is Long -> LuaValue.valueOf(value.toInt())
        is Float -> LuaValue.valueOf(value.toDouble())
        is Double -> LuaValue.valueOf(value)
        is ByteArray -> LuaValue.valueOf(value)
        is String -> LuaValue.valueOf(value)
        else -> LuaValue.userdataOf(value)
    }
}
