package io.pratik

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread


fun main() = runBlocking{
    println("My program runs...: ${Thread.currentThread().name}")

    launch {
        longRunningTask()
    }

    println("My program run ends...: " +
            "${Thread.currentThread().name}")
}

suspend fun longRunningTask(){
    println("executing longRunningTask on...: ${Thread.currentThread().name}")
    delay(1000)
    println("longRunningTask ends on thread ...: ${Thread.currentThread().name}")
}

