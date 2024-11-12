package com.example.hope

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.hope.mood_tracker.NoteApp
import com.example.hope.mood_tracker.ui.theme.HopeTheme

class MainActivity : ComponentActivity() {

//    private val db by lazy {
//        Room.databaseBuilder(
//            applicationContext,
//            NoteDatabase::class.java,
//            "notes.db"
//        ).build()
//    }
//
//    private val viewModel by viewModels<NoteViewModel>(
//        factoryProducer = {
//            object : ViewModelProvider.Factory {
//                override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                    return NoteViewModel(db.noteDao) as T
//                }
//            }
//        }
//    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HopeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NoteApp()
                }
            }
        }
    }
}