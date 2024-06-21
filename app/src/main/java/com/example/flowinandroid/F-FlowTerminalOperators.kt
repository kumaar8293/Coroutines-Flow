package com.example.flowinandroid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() {
    val job1 = CoroutineScope(Dispatchers.IO).launch {
        val firstUser =
            userProducer().first() //It will return only 1st data and if there's no data then it will throw exception
        println("$firstUser")

        val firstOrNull = userProducerNull().firstOrNull() //It will return only 1st data or null
        println("$firstOrNull")

        val toList = userProducer().toList() //It will convert output to a list
        println("$toList")

        val toListNull = userProducerNull().toList()// //Return a list or emptyList
        println("$toListNull")
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

private fun userProducerNull() = flow<User> {

}

private suspend fun provideUser(id: Int): User {
    delay(1000)
    return User(id, "UserName$id")
}

/** OUTPUT
 * User(id=1, name=UserName1)
 * null
 * [User(id=1, name=UserName1), User(id=2, name=UserName2), User(id=3, name=UserName3), User(id=4, name=UserName4), User(id=5, name=UserName5)]
 * []
 */