package com.example.flowinandroid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() = runBlocking{
    val users: Flow<User> = userProducer()
    val job = CoroutineScope(Dispatchers.IO).launch {
        users.collect {
            println("LENSA $it")
        }
    }
    runBlocking {
        job.join()
    }

}

private fun userProducer() = flow<User> {
    println("inside producer")
    for (i in 1..5) {
        val user = provideUser(i)
        emit(user)
    }
}

private suspend fun provideUser(id: Int): User {
    delay(1000)
    println("LENSA $id")
    return User(id, "UserName$id")
}

/** Output
 * I  LENSA 1
 * I  LENSA User(id=1, name=UserName1)
 * I  LENSA 2
 * I  LENSA User(id=2, name=UserName2)
 * I  LENSA 3
 * I  LENSA User(id=3, name=UserName3)
 * I  LENSA 4
 * I  LENSA User(id=4, name=UserName4)
 * I  LENSA 5
 * I  LENSA User(id=5, name=UserName5)
 */
