package br.tiagohm.restler.ui.page

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.tiagohm.restler.ui.theme.BroomIcon
import br.tiagohm.restler.ui.theme.ContentSaveEditIcon
import br.tiagohm.restler.ui.theme.TabIcon
import br.tiagohm.restler.ui.theme.TabPlusIcon

enum class HomeMenuItem {
    MENU,
    SEND,
    NEW_TAB,
    DUPLICATE_TAB,
    RESTORE_TAB,
    CLEAR,
    SAVE_AS,
    SETTINGS,
}

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun HomePage(onMenuItemClick: (HomeMenuItem) -> Unit) {
    Scaffold(
        topBar = {
            var menuOptionsExpanded by remember { mutableStateOf(false) }

            TopAppBar(
                title = {
                    Text(text = "Home")
                },
                navigationIcon = {
                    IconButton(onClick = { onMenuItemClick(HomeMenuItem.MENU) }) {
                        Icon(Icons.Filled.Menu, "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onMenuItemClick(HomeMenuItem.SEND)
                        menuOptionsExpanded = false
                    }) {
                        Icon(Icons.Filled.Send, "Send")
                    }
                    IconButton(onClick = { menuOptionsExpanded = true }) {
                        Icon(Icons.Filled.MoreVert, "Options")
                    }
                    DropdownMenu(
                        expanded = menuOptionsExpanded,
                        onDismissRequest = { menuOptionsExpanded = false }
                    ) {
                        DropdownMenuItem(
                            leadingIcon = { TabPlusIcon() },
                            text = { Text("New tab") },
                            onClick = {
                                onMenuItemClick(HomeMenuItem.NEW_TAB)
                                menuOptionsExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = { TabPlusIcon() },
                            text = { Text("Duplicate tab") },
                            onClick = {
                                onMenuItemClick(HomeMenuItem.DUPLICATE_TAB)
                                menuOptionsExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = { TabIcon() },
                            text = { Text("Restore tab") },
                            onClick = {
                                onMenuItemClick(HomeMenuItem.RESTORE_TAB)
                                menuOptionsExpanded = false
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            leadingIcon = { BroomIcon() },
                            text = { Text("Clear") },
                            onClick = {
                                onMenuItemClick(HomeMenuItem.CLEAR)
                                menuOptionsExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = { ContentSaveEditIcon() },
                            text = { Text("Save as...") },
                            onClick = {
                                onMenuItemClick(HomeMenuItem.SAVE_AS)
                                menuOptionsExpanded = false
                            }
                        )
                        Divider()
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Filled.Settings, "Settings") },
                            text = { Text("Settings") },
                            onClick = {
                                onMenuItemClick(HomeMenuItem.SETTINGS)
                                menuOptionsExpanded = false
                            }
                        )
                    }
                }
            )
        }
    ) {
        Text(
            text = "Hello Android!",
        )
    }
}
