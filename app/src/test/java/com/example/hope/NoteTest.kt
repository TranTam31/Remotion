package com.example.hope

import com.example.hope.mood_tracker.data.database.Note
import com.example.hope.mood_tracker.data.database.NoteDao
import com.example.hope.mood_tracker.data.repository.NoteRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.LocalDate
import com.google.common.truth.Truth.assertThat

class NoteTest {
    private lateinit var noteDao: NoteDao
    private lateinit var noteRepository: NoteRepository

    @Before
    fun setUp() {
        noteDao = mock(NoteDao::class.java)
        noteRepository = mock(NoteRepository::class.java)
    }

    @Test
    fun getNoteById() {
        val note = Note(
            userId = "1",
            emotion = 2,
            content = "Test content",
            date = LocalDate.now()
        )
        `when`(noteDao.getNotes(note.userId)).thenReturn(flowOf(listOf(note)))

        val result = runBlocking { noteDao.getNotes(note.userId).toList().first()}
        val equal = result.first()
        assertThat(equal).isEqualTo(note)

    }
    @Test
    fun insertNote() {
        val note = Note(
            userId = "1",
            emotion = 2,
            content = "Test content",
            date = LocalDate.now()
        )
        runBlocking {
            noteRepository.insertNote(note)
            `when`(noteDao.getNotes(note.userId)).thenReturn(flowOf(listOf(note)))
            val result = noteDao.getNotes(note.userId).toList().first()
            val equal = result.first()
            assertThat(equal).isEqualTo(note)
        }
    }

    @Test
    fun updateNote() {
        val note = Note(
            userId = "1",
            emotion = 2,
            content = "Test content",
            date = LocalDate.now()
        )
        runBlocking {
            noteRepository.updateNote(note)
            `when`(noteDao.getNotes(note.userId)).thenReturn(flowOf(listOf(note)))
            val result = noteDao.getNotes(note.userId).toList().first()
            val equal = result.first()
            assertThat(equal).isEqualTo(note)
        }
    }
}