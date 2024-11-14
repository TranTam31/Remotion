package com.example.hope.mood_tracker.data.repository

import com.example.hope.auth.ui.sign_in.GoogleAuthUiClient
import com.example.hope.auth.ui.sign_in.UserData
import com.example.hope.mood_tracker.data.database.Note
import com.example.hope.mood_tracker.data.database.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    googleAuthUiClient: GoogleAuthUiClient
) : NoteRepository {
    override val userData: UserData = googleAuthUiClient.getCurrentUser()

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getNotes(userId = userData.userId)
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        return noteDao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        return noteDao.deleteNote(note)
    }
}