package com.example.flowinandroid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * In Kotlin, channels are used for communication between coroutines.
 * If you want to send data to a channel without having a receiver ready to
 * consume it immediately, you might be running into issues due to the
 * default behavior of Channel.
 *
 * By default, channels are rendezvous channels,
 * meaning they only buffer one element and suspend the sender until the element is received.
 *
 * If we don't have immediate Receiver, We can also give the initial capacity to buffer some data.
 */
val channel = Channel<Int>()
fun main() {
    val job = CoroutineScope(Dispatchers.IO).launch {
        producer()
        // receiver()
    }

    runBlocking {
        //Wait for other coroutine to finish
        job.join()
    }
}

private fun producer() {
    CoroutineScope(Dispatchers.IO).launch {
        println("Producer started")
        channel.send(1)
        println("Producer send 1")
        channel.send(2)
        println("Producer send 2")
        println("Producer execute")
    }
}

private fun receiver() {
    CoroutineScope(Dispatchers.IO).launch {
        println("Receiver 1 ${channel.receive()}")
        println("Receiver 2 ${channel.receive()}")
    }
}

//If we comment the receiver() then it will print only "Producer started",
// then send will be suspended different coroutines

/** Output
 * NOTE: Output can be changed, because producer and receiver both are in different coroutine
 * Sometimes, may be value has been received then the send printed
 *
 * Producer started
 * Producer send 1
 * Receiver 1 1
 * Receiver 2 2
 * Producer send 2
 * Producer execute
 */