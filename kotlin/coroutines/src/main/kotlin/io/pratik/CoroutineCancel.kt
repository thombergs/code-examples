package io.pratik

import kotlinx.coroutines.*
import java.io.File

fun main() = runBlocking{
    println("program runs...: ${Thread.currentThread().name}")

    val job:Job = launch {
        val files = File ("/Users/10680240/Downloads/").listFiles()
        var loop = 0

        while (loop < files.size-1 ) {
            if(isActive) {
                readFile(files.get(++loop))
            }
        }
    }
    delay(1500)
    job.cancelAndJoin()

    println("program run ends...: ${Thread.currentThread().name}")
}

suspend fun readFile(file: File) {
    println("reading file ${file.name}")
    if (file.isFile) {
        // process file
    }
    delay(100)
}

