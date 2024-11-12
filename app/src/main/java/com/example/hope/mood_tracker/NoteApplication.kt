package com.example.hope.mood_tracker

import android.app.Application
import com.example.hope.mood_tracker.data.AppContainer
import com.example.hope.mood_tracker.data.AppDataContainer

class NoteApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}