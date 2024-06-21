package com.example.flowinandroid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * MutableStateFlow is HOT in nature
 * Even there is no collector, producer will start producing the value
 * State flow keeps the last emitted value.
 */
fun main() {
    val job1 = CoroutineScope(Dispatchers.IO).launch {
        val users = userProducer()
        delay(7000)
        users.collect {
            println("Collected $it")
        }
        /**
         * If receiver joins in between emitting then it will get the rest value,
         * If all the value has been emitted and receiver joins late, receiver will get the last value.
         *
         * If there is default value while creating  MutableStateFlow(10), then we can get this value
         * users.value
         */
    }

    runBlocking {
        job1.join()
    }
}

private fun userProducer(): StateFlow<Int> {
    val sharedFlow = MutableStateFlow(10)
    GlobalScope.launch {
        for (i in 1..5) {
            println("Emitted $i")
            sharedFlow.emit(i)
            delay(1000)
        }
    }
    return sharedFlow
}


