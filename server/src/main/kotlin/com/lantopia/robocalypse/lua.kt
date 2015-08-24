package com.lantopia.robocalypse

import com.lantopia.robocalypse.model.Program
import com.lantopia.robocalypse.model.Robot
import org.luaj.vm2.*
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.VarArgFunction
import org.luaj.vm2.lib.jse.JsePlatform

class LuaProgram(script: String, robot: Robot) : Program(script, robot) {
    private val compiledScript: LuaValue

    init {
        val globals = JsePlatform.standardGlobals() // TODO: Prune

        robot.components.forEach { component ->
            globals.get("require").call(
                    LuaValue.valueOf(component.name),
                    object : TwoArgFunction() {
                        override fun call(modname: LuaValue, env: LuaValue): LuaValue? {
                            val library = LuaValue.tableOf(
                                component.functions.flatMap { function ->
                                    listOf(LuaValue.valueOf(function.name),
                                            object : VarArgFunction() {
                                                override fun invoke(args: Varargs?) : Varargs? {
                                                    // TODO: Build 'real' array from varargs and invoke
                                                    if (args == null)
                                                    0.rangeTo(args?.narg()?.minus(1))
                                                    return toLuaValue(function.invoke(robot, args.))
                                                }
                                            }

                                            // TODO: This might not be needed
                                            when (function.args) {
                                                0 -> object : ZeroArgFunction() {
                                                        override fun call(): LuaValue? {
                                                            return toLuaValue(function.invoke(robot))
                                                        }
                                                    }
                                                1 -> object : OneArgFunction() {
                                                    override fun call(p0: LuaValue?): LuaValue? {
                                                        return toLuaValue(function.invoke(robot, fromLuaValue(p0)))
                                                    }
                                                }
                                                2 -> object : TwoArgFunction() {
                                                    override fun call(p0: LuaValue?, p1: LuaValue?): LuaValue? {
                                                        return toLuaValue(function.invoke(robot, fromLuaValue(p0)))
                                                    }
                                                }
                                                else -> throw UnsupportedOperationException()
                                            })
                                }.toTypedArray()
                            )
                            env.set(modname, library)
                            return library
                        }
                    })
        }

        compiledScript = globals.load(script)
    }

    private fun fromLuaValue(value: LuaValue?): Any? {
        return when (value) {
            null -> null
            LuaValue.NIL -> null
            is LuaBoolean -> value.booleanValue()
            is LuaInteger -> value.tolong()
            is LuaDouble -> value.todouble()
            is LuaString -> value.tojstring()
            is LuaTable -> mapOf(value.keys().map {
                    Pair(it, fromLuaValue(value.get(it)))
                }.toTypedArray())
            is LuaThread, is LuaFunction
                -> throw UnsupportedOperationException("thread, function not yet supported")
            else -> value.touserdata()
        }
    }

    /** Convert any normal Java/Kotlin type, or null, to a reasonable LuaValue instance. */
    private fun toLuaValue(value: Any?): LuaValue? {
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

    override fun execute(tick: Int) {
        // Globals exposes the Lua standard library, which we would like to enhance by adding routines exposed by
        // robot components.

    }
}
