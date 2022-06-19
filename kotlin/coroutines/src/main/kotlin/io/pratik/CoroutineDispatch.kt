package io.pratik

import kotlinx.coroutines.*

fun main() = runBlocking {
   /* launch {
        longTask()
    }*/

    /*launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
        println("Unconfined : running in thread ${Thread.currentThread().name}")
        longTask()
    }*/
    /*launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher
        println("Default  : running in thread ${Thread.currentThread().name}")
        longTask()
    }*/
    launch(newSingleThreadContext("MyThread")) { // will get its own new thread
        println("newSingleThreadContext: running in  thread ${Thread.currentThread().name}")
        longTask()
    }
    println("completed tasks")
}

// Concurrently executes both sections
/*suspend fun longTask()  {
    launch { // context of the parent, main runBlocking coroutine
        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
        longTask()
    }
    launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher
        println("Default               : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
    }
    println("completed task1 and task2")
}*/

suspend fun longTask(){
    println("executing longTask on...: ${Thread.currentThread().name}")
    delay(1000)
    println("longTask ends on thread ...: ${Thread.currentThread().name}")
}

