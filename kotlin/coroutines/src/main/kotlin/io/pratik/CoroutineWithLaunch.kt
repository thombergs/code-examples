package io.pratik

import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext

fun main() = runBlocking{
    println("My program runs...: ${Thread.currentThread().name}")

    val job:Job = launch (EmptyCoroutineContext, CoroutineStart.DEFAULT){
        longRunningTaskSuspended()
    }

    job.join()

    println("My program run ends...: ${Thread.currentThread().name}")
}

suspend fun longRunningTaskSuspended(){
    println("executing longRunningTask on...: ${Thread.currentThread().name}")
    delay(1000)
    println("longRunningTask ends on thread ...: ${Thread.currentThread().name}")
}