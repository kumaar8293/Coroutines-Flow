package com.example.flowinandroid

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() {
    val job1 = CoroutineScope(Dispatchers.IO).launch {
        provideNotes()
            .map {
                //I am coping all the data to a new formatted class and title to uppercase
                FormattedNote(it.id, it.isActive, it.title.uppercase(), it.description)
                //We can map our data or change it's value like id = id*2, we can play with the object before returning
            }
            .filter {
                it.isActive
                //We can filter the response based on some logic
            }
            .collect {
                println("Final Formatted Book $it")
            }

    }
    runBlocking {
        job1.join()
    }
}

private fun provideNotes(): Flow<Note> {
    val notes = mutableListOf<Note>()
    notes.add(Note(1, true, "First", "First Description"))
    notes.add(Note(2, true, "Second", "Second Description"))
    notes.add(Note(3, false, "Third", "Third Description"))
    notes.add(Note(4, true, "Fourth", "Fourth Description"))
    return notes.asFlow()
}

private data class Note(
    val id: Int,
    val isActive: Boolean,
    val title: String,
    val description: String
)

private data class FormattedNote(
    val id: Int,
    val isActive: Boolean,
    val title: String,
    val description: String
)
/** OUTPUT  (id=3 is filtered because its not active)
 * Final Formatted Book FormattedNote(id=1, isActive=true, title=FIRST, description=First Description)
 * Final Formatted Book FormattedNote(id=2, isActive=true, title=SECOND, description=Second Description)
 * Final Formatted Book FormattedNote(id=4, isActive=true, title=FOURTH, description=Fourth Description)
 *
 */