package com.example.flowinandroid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Live Data , Transformation on Main thread.
 * All the operators runs on main thread {map,filter,etc}
 * LifeCycle dependent
 * Disadvantage : Suppose if we have any heavy task inside map and filter then it will block the main thread, Impact performance
 * Limited operators inside LiveData
 * We can't use liveData inside repositories because its lifecycle dependent
 */

fun main() {
    val job1 = CoroutineScope(Dispatchers.IO).launch {
        val users = userProducer()
        delay(7000)
        users.collect {
            println("Collected $it")
        }
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


