package io.pratik

import kotlinx.coroutines.*
import kotlin.coroutines.ContinuationInterceptor

fun main() = runBlocking {
    println(coroutineContext)
    launch {
        println(
            "launch default: running in  thread ${Thread.currentThread().name} ${coroutineContext[ContinuationInterceptor]}")
        longTask()
    }

   /* launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
        println("Unconfined : running in thread ${Thread.currentThread().name}")
        longTask()
    }*/
    /*repeat(1000) {
        val dispatcher = Dispatchers.Default.limitedParallelism(3)
        launch(dispatcher) {
       // launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher
            println("Default  : running in thread ${Thread.currentThread().name}")
            longTask()
        }
    }*/
  /* launch(newSingleThreadContext("MyThread")) { // will get its own new thread
        println("newSingleThreadContext: running in  thread ${Thread.currentThread().name}")
        longTask()
    }*/
    println("completed tasks")
}


suspend fun longTask(){
    println("executing longTask on...: ${Thread.currentThread().name}")
    delay(1000)
    println("longTask ends on thread ...: ${Thread.currentThread().name}")
}

