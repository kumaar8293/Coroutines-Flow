package com.example.flowinandroid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/** Internally Flow keeps the CoroutineContext
 * If we want to switch the context we need to use flowOn() operators
 *
 */
fun main() {
    val job1 = CoroutineScope(Dispatchers.IO).launch {
        try {
            userProducer()
                .collect {
                    println("job1 collector $it")
                }
        } catch (exception: Exception) {
            println("job1 collector exception ${exception.localizedMessage}")
        }
    }

    val job2 = CoroutineScope(Dispatchers.IO).launch {
        userProducerWithExceptionHandling()
            .collect {
                println("job2 collector $it")
            }
    }
    runBlocking {
        job1.join()
        job2.join()
    }
}

private fun userProducer() = flow {
    for (i in 1..5) {
        delay(1000)
        println("job1 emitter $i")
        if (i == 3) {
            throw Exception("Exception in job1 emitter")
        }
        emit(i)
    }
}

private fun userProducerWithExceptionHandling() = flow {
    for (i in 1..5) {
        delay(1000)
        println("Job2 emitter $i")
        if (i == 3) {
            throw Exception("Exception in emitter")
        }
        emit(i)
    }
}.catch {
    println("job2 emitter exception handling")
    //We can handle the exception and emit some data
    emit(-1)
}

/**
 * 1- We can handle exception using try catch on collector
 *
 * 2- We can also handle the exception on producer side
 * In producer side we can have multiple catch block.
 * We can emit the exception data accordingly
 */