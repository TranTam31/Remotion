package com.example.hope

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hope.mood_tracker.NoteApp
import com.example.hope.theme.HopeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HopeTheme {
                Scaffold (
                    modifier = Modifier.fillMaxSize(),
                ) {padding ->
                    RemotionApp(modifier = Modifier.padding(padding))
                }
            }
        }
    }
}

//@SuppressLint("NewApi")
//@Composable
//fun Screen(modifier: Modifier = Modifier) {
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            ModalDrawerSheet(modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp*4/5)) {
//                DrawableContent()
//            }
//        }
//    ) {
//        Scaffold(topBar = {
//            TopBar(
//                onOpenDrawer = {
//                scope.launch {
//                    drawerState.apply {
//                        if(isClosed) open() else open()
//                    }
//                }
//            })
//        }
//        ) { padding ->
//            NoteApp(modifier=Modifier.padding(padding))
//        }
//    }
//}
//
//@Composable
//fun DrawableContent(modifier: Modifier = Modifier) {
//    Text(
//        text = "Remotion",
//        fontSize = 24.sp,
//        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 16.dp)
//    )
//    HorizontalDivider()
//    Spacer(modifier = Modifier.height(4.dp).padding(top = 16.dp))
//    NavigationDrawerItem(
//        icon = {
//            Icon(
//                imageVector = Icons.Default.Favorite,
//                contentDescription = null,
//                modifier = Modifier
//                    .padding(start = 8.dp, end = 2.dp)
//                    .size(20.dp)
//            )
//        },
//        label = {
//            Text(
//                text = "Mood Tracker",
//                fontSize = 18.sp
//            )
//        },
//        selected = true,
//        onClick = {}
//    )
//    Spacer(modifier = Modifier.height(4.dp))
//    NavigationDrawerItem(
//        icon = {
//            Icon(
//                imageVector = Icons.Default.DateRange,
//                contentDescription = null,
//                modifier = Modifier
//                    .padding(start = 8.dp, end = 2.dp)
//                    .size(20.dp)
//            )
//        },
//        label = {
//            Text(
//                text = "Reminder",
//                fontSize = 18.sp
//            )
//        },
//        selected = false,
//        onClick = {}
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TopBar(onOpenDrawer: () -> Unit) {
//    TopAppBar(
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.surfaceContainer
//        ),
//        navigationIcon = {
//            Icon(
//                imageVector = Icons.Default.Menu,
//                contentDescription = null,
//                modifier = Modifier
//                    .padding(start = 16.dp, end = 4.dp)
//                    .size(30.dp)
//                    .clickable{
//                        onOpenDrawer()
//                }
//            )
//        },
//        title = {
//            Text(text = "Have a nice day")
//        }
//    )
//}