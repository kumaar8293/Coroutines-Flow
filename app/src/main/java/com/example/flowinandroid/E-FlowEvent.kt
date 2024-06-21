package com.example.flowinandroid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() {
    val job1 = CoroutineScope(Dispatchers.IO).launch {
        val users: Flow<User> = userProducer()
        users
            .onStart {
                //When flow start
                // We can also do manual emit inside onStart and onCompletion
              //  emit(User(-1,"User-1"))
                println("onStart starting out")

            }.onCompletion {
                // We can also do manual emit before it start
              //  emit(User(-6,"User-6"))
                println("onCompletion")
            }.onEach {
                //This will be called before emit for each emit
                println("onEach About to emit ${it.name}")

            }
            .collect {
                println("collect ${it.name}")
            }
    }
    runBlocking {
        job1.join()
    }

}

private fun userProducer() = flow<User> {
    for (i in 1..5) {
        val user = provideUser(i)
        emit(user)
    }
}

private suspend fun provideUser(id: Int): User {
    delay(1000)
    return User(id, "UserName$id")
}
/** OUTPUT
 * onStart starting out
 * onEach About to emit UserName1
 * collect UserName1
 * onEach About to emit UserName2
 * collect UserName2
 * onEach About to emit UserName3
 * collect UserName3
 * onEach About to emit UserName4
 * collect UserName4
 * onEach About to emit UserName5
 * collect UserName5
 * onCompletion
 */