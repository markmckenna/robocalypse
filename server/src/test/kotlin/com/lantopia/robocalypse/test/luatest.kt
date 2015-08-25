package com.lantopia.robocalypse.test

import com.lantopia.robocalypse.*
import org.luaj.vm2.LuaValue
import org.junit.Test as test
import org.assertj.core.api.Assertions as aj

class LuaValueTest {
    test fun fromLuaValueTest() {
        aj.assertThat(fromLuaValue(LuaValue.NIL)).isEqualTo(null)
    }

    test fun toLuaValueTest() {
        aj.assertThat(toLuaValue(null)).isEqualTo(LuaValue.NIL)
    }
}
