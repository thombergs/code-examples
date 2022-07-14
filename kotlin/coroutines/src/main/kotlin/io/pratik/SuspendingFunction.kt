package io.pratik

import kotlinx.coroutines.*
import java.time.Instant
import kotlin.concurrent.thread


fun main() = runBlocking{
    println("${Instant.now()}: My program runs...: ${Thread.currentThread().name}")
    val productId = findProduct()

    launch (Dispatchers.Unconfined) {
        val price = fetchPrice(productId)
    }
    updateProduct()
    println("${Instant.now()}: My program run ends...: " +
            "${Thread.currentThread().name}")
}

suspend fun fetchPrice(productId: String) : Double{
    println("${Instant.now()}: fetchPrice starts on...: ${Thread.currentThread().name} ")
    delay(2000)
    println("${Instant.now()}: fetchPrice ends on...: ${Thread.currentThread().name} ")
    return 234.5
}

fun findProduct() : String{
    println("${Instant.now()}: findProduct on...: ${Thread.currentThread().name}")
    return "P12333"
}

fun updateProduct() : String{
    println("${Instant.now()}: updateProduct on...: ${Thread.currentThread().name}")
    return "Product updated"
}

