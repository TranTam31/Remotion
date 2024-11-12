package com.example.hope.mood_tracker.ui

import java.time.LocalDate

sealed interface NoteEvent {
    object SaveNote: NoteEvent
    data class SetContent(val content: String): NoteEvent
    data class SetDate(val date: LocalDate): NoteEvent
    object ShowDialog: NoteEvent
    object HideDialog: NoteEvent
}