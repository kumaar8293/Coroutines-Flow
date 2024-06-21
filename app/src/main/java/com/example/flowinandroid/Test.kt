package com.example.flowinandroid

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


fun main() = runBlocking {
    val job = launch(Dispatchers.Default) {

        try {
            for (i in 0..500) {
                println("$i,")
                delay(5) // we can use yield() as well
            }
        } catch (cancellation: CancellationException) {
            println("\n Exception caught safely ${cancellation.message}")
        } finally {
          //  delay(100) // Now main thread will wait
            println("closed")
            println("\n Close resource in finally")
        }
    }
    delay(100)
    job.cancelAndJoin()

    println("CODE finished")
}