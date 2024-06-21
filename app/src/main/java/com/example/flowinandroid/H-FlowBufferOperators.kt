package com.example.flowinandroid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.measureTime


fun main() {
    val job1 = CoroutineScope(Dispatchers.IO).launch {
        val time = measureTime {
            userProducer().buffer(-1)
                //Without it will take total ~12 seconds [7.5 for collect and 5 second to producer]
                //With buffer time will reduce ~8.5 seconds[7.5 for collect and 1 second for producer]
                //Buffer capacity can be change as per requirement
                .collect {
                    delay(1500)
                    println("collect $it")
                }

        }
        println(time)
    }
    runBlocking {
        job1.join()
    }
}

private fun userProducer() = flow<Int> {
    for (i in 1..5) {
        delay(1000)
        emit(i)
    }
}
/** Output without Buffer
 * collect 1
 * collect 2
 * collect 3
 * collect 4
 * collect 5
 * 12.562653917s
 */

/** Output with Buffer
 * collect 1
 * collect 2
 * collect 3
 * collect 4
 * collect 5
 * 8.569122666s
 *
 */