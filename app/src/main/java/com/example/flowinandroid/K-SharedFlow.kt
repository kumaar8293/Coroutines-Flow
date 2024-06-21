package com.example.flowinandroid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * MutableSharedFlow is HOT in nature
 * Even there is no collector, producer will start producing the value
 */

fun main() {
    val job1 = CoroutineScope(Dispatchers.IO).launch {
        val users = userProducer()
        users.collect {
            println("Job 1 collect $it")
        }
    }

    val job2 = CoroutineScope(Dispatchers.IO).launch {
        val users = userProducer()
        delay(2500) //Second collector will get the rest of values
        users.collect {
            println("Job 2 collect $it")
        }
    }

    runBlocking {
        job1.join()
        job2.join()
    }

}


/**
 * NOTE : Functions returning "Flow" or "Channel" should not be suspending
 *
 * We have another feature of replay, If we want to provide some previous data to the late joiner(Late collector)
 * i.e : val sharedFlow = MutableSharedFlow<User>(1), in this case second job will get one previous value also
 */
private fun userProducer():Flow<User> {
    val sharedFlow = MutableSharedFlow<User>()
    GlobalScope.launch {
        for (i in 1..5) {
            val user = provideUser(i)
            sharedFlow.emit(user)
        }
    }
    return sharedFlow
}

private suspend fun provideUser(id: Int): User {
    delay(1000)
    return User(id, "UserName$id")
}

/** Output
 * Job 1 collect User(id=1, name=UserName1)
 * Job 1 collect User(id=2, name=UserName2)
 * Job 2 collect User(id=3, name=UserName3)
 * Job 1 collect User(id=3, name=UserName3)
 * Job 2 collect User(id=4, name=UserName4)
 * Job 1 collect User(id=4, name=UserName4)
 * Job 2 collect User(id=5, name=UserName5)
 * Job 1 collect User(id=5, name=UserName5)
 */
