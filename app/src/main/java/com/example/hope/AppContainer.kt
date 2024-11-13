package com.example.hope

import android.content.Context
import com.example.hope.mood_tracker.data.database.NoteDatabase
import com.example.hope.mood_tracker.data.repository.NoteRepository
import com.example.hope.mood_tracker.data.repository.NoteRepositoryImpl

//cung cấp một nơi duy nhất để quản lý và
// khởi tạo các đối tượng cần thiết cho ứng dụng (như repository và database)
interface AppContainer {
    val noteRepository: NoteRepository
}

class AppDataContainer(private val context: Context): AppContainer {

    override val noteRepository: NoteRepository by lazy {
        NoteRepositoryImpl(NoteDatabase.getDatabase(context).noteDao)
    }
}