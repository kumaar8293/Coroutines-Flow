package com.example.flowinandroid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val job = CoroutineScope(Dispatchers.IO).launch {
        val users = userProducer()
        //After ~4 second we will get the list of users
        users.forEach {
            //Now we can perform the action on users
            println("LENSA $it")
        }
    }

    runBlocking {
        //Wait for other coroutine to finish
        job.join()
    }
}

//This will take more than 4 second to execute
private suspend fun userProducer(): List<User> {
    val users = mutableListOf<User>()
    users.add(provideUser(1))
    users.add(provideUser(2))
    users.add(provideUser(3))
    users.add(provideUser(4))
    return users
}

private suspend fun provideUser(id: Int): User {
    delay(1000)// Do network call
    println("LENSA provideUser $id")
    return User(id, "UserName$id")
}


/** Final Output
 * LENSA provideUser 1
 * LENSA provideUser 2
 * LENSA provideUser 3
 * LENSA provideUser 4
 * LENSA User(id=1, name=UserName1)
 * LENSA User(id=2, name=UserName2)
 * LENSA User(id=3, name=UserName3)
 * LENSA User(id=4, name=UserName4)
 */
