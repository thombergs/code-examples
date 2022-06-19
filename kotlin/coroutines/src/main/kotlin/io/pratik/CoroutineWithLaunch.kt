package io.pratik

import kotlinx.coroutines.*

fun main() = runBlocking{
    println("My program runs...: ${Thread.currentThread().name}")

    val job:Job = launch {
        longRunningTaskSuspended()
    }

    job.join()
    /*runBlocking {
        delay(2000)
    }*/

    println("My program run ends...: ${Thread.currentThread().name}")
}

suspend fun longRunningTaskSuspended(){
    println("executing longRunningTask on...: ${Thread.currentThread().name}")
    delay(1000)
    println("longRunningTask ends on thread ...: ${Thread.currentThread().name}")
}