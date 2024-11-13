package com.example.hope

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hope.mood_tracker.NoteApp
import com.example.hope.navigation.Destinations
import com.example.hope.reminder.ReminderApp
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RemotionApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Để lưu trạng thái mục đã chọn
    var selectedDestination by remember { mutableStateOf(Destinations.NOTE_APP) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp*4/5)) {
                DrawableContent(
                    selectedDestination = selectedDestination,
                    onDestinationSelected = { destination ->
                        selectedDestination = destination
                        navController.navigate(destination)
                        scope.launch { drawerState.close() }  // Đóng drawer sau khi điều hướng
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    onOpenDrawer = {
                        // You can toggle the drawer here as needed
                    }
                )
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Destinations.NOTE_APP,
                modifier = Modifier.padding(padding)
            ) {
                composable(Destinations.NOTE_APP) {
                    NoteApp() // NoteApp will be shown here
                }
                composable(Destinations.REMINDER_APP) {
                    ReminderApp() // ReminderApp will be shown here
                }
            }
        }
    }
}

@Composable
fun DrawableContent(
    selectedDestination: String,
    onDestinationSelected: (String) -> Unit
) {
    Text(
        text = "Remotion",
        fontSize = 24.sp,
        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 16.dp)
    )
    HorizontalDivider()
    Spacer(modifier = Modifier.height(4.dp).padding(top = 16.dp))

    // Mood Tracker item
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp, end = 2.dp).size(20.dp)
            )
        },
        label = {
            Text(text = "Mood Tracker", fontSize = 18.sp)
        },
        selected = selectedDestination == Destinations.NOTE_APP,  // Kiểm tra xem mục này có được chọn không
        onClick = {
            onDestinationSelected(Destinations.NOTE_APP)
        }
    )
    Spacer(modifier = Modifier.height(4.dp))

    // Reminder item
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp, end = 2.dp).size(20.dp)
            )
        },
        label = {
            Text(text = "Reminder", fontSize = 18.sp)
        },
        selected = selectedDestination == Destinations.REMINDER_APP,  // Kiểm tra xem mục này có được chọn không
        onClick = {
            onDestinationSelected(Destinations.REMINDER_APP)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onOpenDrawer: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp, end = 4.dp)
                    .size(30.dp)
                    .clickable { onOpenDrawer() }
            )
        },
        title = { Text(text = "Have a nice day") }
    )
}