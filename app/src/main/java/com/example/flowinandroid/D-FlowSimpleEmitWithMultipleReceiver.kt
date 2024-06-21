package com.example.flowinandroid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() {
    val job1 = CoroutineScope(Dispatchers.IO).launch {
        val users: Flow<User> = userProducer("Job1")
        users.collect {
            println("Job 1 collect $it")
        }
    }

    val job2 = CoroutineScope(Dispatchers.IO).launch {
        val users: Flow<User> = userProducer("Job2")
        delay(2000) //Second collector will get also the values from starting
        users.collect {
            println("Job 2 collect $it")
        }
    }

    runBlocking {
        job1.join()
        job2.join()
    }

}

private fun userProducer(jobName: String) = flow<User> {
    println("\ninside producer")
    for (i in 1..5) {
        val user = provideUser(i, jobName)
        emit(user)
    }
}

private suspend fun provideUser(id: Int, jobName: String): User {
    delay(1000)
    println("$jobName $id")
    return User(id, "UserName$id")
}

/** Output
 * inside producer
 * Job1 1
 * Job 1 collect User(id=1, name=UserName1)
 *
 * inside producer
 * Job1 2
 * Job 1 collect User(id=2, name=UserName2)
 * Job2 1
 * Job 2 collect User(id=1, name=UserName1)
 * Job1 3
 * Job 1 collect User(id=3, name=UserName3)
 * Job2 2
 * Job1 4
 * Job 2 collect User(id=2, name=UserName2)
 * Job 1 collect User(id=4, name=UserName4)
 * Job2 3
 * Job 2 collect User(id=3, name=UserName3)
 * Job1 5
 * Job 1 collect User(id=5, name=UserName5)
 * Job2 4
 * Job 2 collect User(id=4, name=UserName4)
 * Job2 5
 * Job 2 collect User(id=5, name=UserName5)
 */
