package com.example.hope.mood_tracker.data

import android.content.Context
import com.example.hope.mood_tracker.data.database.NoteDatabase
import com.example.hope.mood_tracker.data.repository.NoteRepository
import com.example.hope.mood_tracker.data.repository.NoteRepositoryImpl

interface AppContainer {
    val noteRepository: NoteRepository
}

class AppDataContainer(private val context: Context): AppContainer {

    override val noteRepository: NoteRepository by lazy {
        NoteRepositoryImpl(NoteDatabase.getDatabase(context).noteDao)
    }
}