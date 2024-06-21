package com.example.flowinandroid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * MutableSharedFlow is HOT in nature
 * Even there is no collector, producer will start producing the value
 */
fun main() {
    val job1 = CoroutineScope(Dispatchers.IO).launch {
        val users = userProducer()
        delay(3000)
        /**
         * userProducer() will take take ~5 seconds and our collectors will start after 3 seconds so O/P will be last value
         * If we increase the delay by 6000 then collector won't get any value. Because all the value has been already emitted
         */
        users.collect {
            println("Collected $it")
        }
    }

    runBlocking {
        job1.join()
    }

}

private fun userProducer(): Flow<Int> {
    val sharedFlow = MutableSharedFlow<Int>()
    GlobalScope.launch {
        for (i in 1..5) {
            println("Emitted $i")
            sharedFlow.emit(i)
            delay(1000)
        }
    }
    return sharedFlow
}


