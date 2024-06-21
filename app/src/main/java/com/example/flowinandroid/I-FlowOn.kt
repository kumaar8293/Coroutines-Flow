package com.example.flowinandroid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/** Internally Flow keeps the CoroutineContext
 * If we want to switch the context we need to use flowOn() operators
 *
 */
fun main() {
    val job1 = CoroutineScope(Dispatchers.IO).launch {
//        userProducerWithContext().collect {
//            delay(1500)
//            println("collect $it")
//        }

        userProducer()
            .map {
                delay(500)
                println("LENSA map $it ${Thread.currentThread().name}")
                it * 2
            }
            .flowOn(Dispatchers.Unconfined)
            .filter {
                delay(600)
                println("LENSA filter $it ${Thread.currentThread().name}")
                it < 8
            }
            .flowOn(Dispatchers.Default)
            .collect {
                println("LENSA collector thread $it ${Thread.currentThread().name}")
            }

        /**
         * In above example we are using multiple flowOn()
         * flowOn() works on upstream side(Means, dispatchers will be applied of the above operators)
         * We can also use multiple flowOn()
         */
    }
    runBlocking {
        job1.join()
    }
}

private fun userProducer() = flow {
    for (i in 1..5) {
        delay(1000)
        println("LENSA emitter thread $i ${Thread.currentThread().name}")
        emit(i)
    }
}

/** OUTPUT
 * LENSA map 1 kotlinx.coroutines.DefaultExecutor
 * LENSA filter 2 DefaultDispatcher-worker-3
 * LENSA collect 2 DefaultDispatcher-worker-3
 * LENSA map 2 kotlinx.coroutines.DefaultExecutor
 * LENSA filter 4 DefaultDispatcher-worker-2
 * LENSA collect 4 DefaultDispatcher-worker-2
 * LENSA map 3 kotlinx.coroutines.DefaultExecutor
 * LENSA filter 6 DefaultDispatcher-worker-2
 * LENSA collect 6 DefaultDispatcher-worker-2
 * LENSA map 4 kotlinx.coroutines.DefaultExecutor
 * LENSA filter 8 DefaultDispatcher-worker-1
 * LENSA map 5 kotlinx.coroutines.DefaultExecutor
 * LENSA filter 10 DefaultDispatcher-worker-1
 */

private fun userProducerWithContext() = flow<Int> {
    withContext(Dispatchers.IO) {
        for (i in 1..5) {
            delay(1000)
            emit(i)
        }
    }
}
/** userProducerWithContext() it will throw the exception, Because we can't change the context directly
 * Fow carries the launch CoroutineCotext
 *
 *
 * Exception in thread "DefaultDispatcher-worker-1" java.lang.IllegalStateException: Flow invariant is violated:
 * 		Flow was collected in [StandaloneCoroutine{Active}@7a3b072d, Dispatchers.IO],
 * 		but emission happened in [kotlinx.coroutines.UndispatchedMarker@4cde0820, UndispatchedCoroutine{Active}@27e16f9, Dispatchers.IO].
 */