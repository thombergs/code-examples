package io.pratik

import kotlinx.coroutines.*
import java.util.UUID

fun main() = runBlocking{
    println("program runs...: ${Thread.currentThread().name}")

    val taskDeferred = async {
        generateUniqueID()
    }
    val taskResult = taskDeferred.await()

    println("program run ends...:  ${taskResult}  ${Thread.currentThread().name}")
}

suspend fun generateUniqueID(): String{
    println("executing generateUniqueID on...: ${Thread.currentThread().name}")
    delay(1000)
    println("generateUniqueID ends on thread ...: ${Thread.currentThread().name}")

    return UUID.randomUUID().toString()
}