package io.pratik

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    executeTasks()
    println("completed all tasks")
}

// Concurrently executes both sections
suspend fun executeTasks() = coroutineScope { // this: CoroutineScope
    launch {
        longRunningTask1()
    }
    launch {
        longRunningTask2()
    }
    println("completed task1 and task2")
}

suspend fun longRunningTask1(){
    println("executing longRunningTask1 on...: ${Thread.currentThread().name}")
    delay(1000)
    println("longRunningTask1 ends on thread ...: ${Thread.currentThread().name}")
}

suspend fun longRunningTask2(){
    println("executing longRunningTask2 on...: ${Thread.currentThread().name}")
    delay(1000)
    println("longRunningTask2 ends on thread ...: ${Thread.currentThread().name}")
}