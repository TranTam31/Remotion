package com.example.hope

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class NoteState constructor(
    val notes: List<Note> = emptyList(),
    val content: String = "",
    val isAddingNote: Boolean = false,
    val date: LocalDate = LocalDate.now()
)
