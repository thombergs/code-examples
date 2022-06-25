package io.pratik

import kotlinx.coroutines.*
import java.io.File


fun main() = runBlocking{
    try {
        val job1 = launch {
            repeat(20){
                println("processing job 1: ${Thread.currentThread().name}")
                yield()
            }
        }

        val job2 = launch {
            repeat(20){
                println("processing job 2: ${Thread.currentThread().name}")
                yield()
            }
        }

        job1.join()
        job2.join()

    } catch (e: CancellationException) {
        // clean up code

    }
}



