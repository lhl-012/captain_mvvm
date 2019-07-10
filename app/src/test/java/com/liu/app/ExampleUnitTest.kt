package com.liu.app

import com.base.captain.LHttp
import com.base.captain.utils.LJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        val json="{\"age\":1,\"adds\":null}"
//        println(LJson.fromJson<User>(json))
        val json="{\"age\":1,\"adds\":{}}"
        println(LJson.fromJson<User>(json))
//        println(LJson.toJson(User(1,Adds(1))))
    }
    data class User(val age:Int?,val adds:Adds?)
    data class Adds(val shop:Int?)
}
