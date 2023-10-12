package br.tiagohm.restler.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.tiagohm.restler.logic.HomeViewModel
import br.tiagohm.restler.logic.ProtocolType
import br.tiagohm.restler.logic.RequestAuthType
import br.tiagohm.restler.logic.RequestBodyType
import br.tiagohm.restler.ui.component.CheckableTab
import br.tiagohm.restler.ui.component.IndicatorChip
import br.tiagohm.restler.ui.dialog.TextFieldDialog
import br.tiagohm.restler.ui.theme.BroomIcon
import br.tiagohm.restler.ui.theme.CallMadeIcon
import br.tiagohm.restler.ui.theme.CallReceivedIcon
import br.tiagohm.restler.ui.theme.ContentSaveEditIcon
import br.tiagohm.restler.ui.theme.Grey800
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
fun HomePage(
    viewModel: HomeViewModel = hiltViewModel(),
    onMenuItemClick: (HomeMenuItem) -> Unit,
) {
    Scaffold(
        topBar = {
            var menuOptionsExpanded by remember { mutableStateOf(false) }

            TopAppBar(
                title = {
                    Text("Home")
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
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Surface(
                modifier = Modifier.padding(8.dp, 2.dp),
                shape = RoundedCornerShape(8.dp),
                tonalElevation = 1.dp
            ) {
                Box {
                    Column {
                        Row {
                            Protocol(viewModel.protocol) { viewModel.protocol = it }

                            var openHttpMethodDialog by remember { mutableStateOf(false) }

                            Method(viewModel.method) {
                                if (it == "CUSTOM") openHttpMethodDialog = true
                                else viewModel.method = it
                            }

                            if (openHttpMethodDialog) {
                                TextFieldDialog(
                                    initialValue = viewModel.customMethod,
                                    title = { Text("HTTP Method") },
                                    label = { Text("Name") },
                                    onConfirm = { viewModel.customMethod = it; openHttpMethodDialog = false },
                                    onDismiss = { openHttpMethodDialog = false }
                                )
                            }
                        }
                        Row {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = viewModel.url,
                                onValueChange = { viewModel.url = it },
                                label = { Text("URL") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }

            var tabIndex by remember { mutableIntStateOf(0) }

            TabRow(selectedTabIndex = tabIndex) {
                LeadingIconTab(
                    selected = tabIndex == 0,
                    text = { Text("REQUEST") },
                    icon = { CallMadeIcon() },
                    onClick = { tabIndex = 0 }
                )
                LeadingIconTab(
                    selected = tabIndex == 1,
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("RESPONSE")
                            Row {
                                IndicatorChip(color = Grey800, text = "0")
                                Spacer(Modifier.width(2.dp))
                                IndicatorChip(color = Grey800, text = "0 ms")
                                Spacer(Modifier.width(2.dp))
                                IndicatorChip(color = Grey800, text = "0 B")
                            }
                        }
                    },
                    icon = { CallReceivedIcon() },
                    onClick = { tabIndex = 1 }
                )
            }

            // Request tab.
            if (tabIndex == 0) {
                var requestTabIndex by remember { mutableIntStateOf(0) }

                ScrollableTabRow(selectedTabIndex = requestTabIndex, edgePadding = 0.dp) {
                    CheckableTab(
                        selected = requestTabIndex == 0,
                        checked = viewModel.requestBodyEnabled,
                        onCheckedChange = { viewModel.requestBodyEnabled = it },
                        onClick = { requestTabIndex = 0 }
                    ) {
                        var expanded by remember { mutableStateOf(false) }

                        Text(viewModel.requestBodyType.name)
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            for (type in RequestBodyType.values()) {
                                DropdownMenuItem(
                                    text = { Text(type.name) },
                                    onClick = { viewModel.requestBodyType = type; expanded = false },
                                )
                            }
                        }
                    }
                    CheckableTab(
                        selected = requestTabIndex == 1,
                        onClick = { requestTabIndex = 1 },
                        checked = viewModel.requestQueryEnabled,
                        onCheckedChange = { viewModel.requestQueryEnabled = it },
                        content = { Text("QUERY") },
                    )
                    CheckableTab(
                        selected = requestTabIndex == 2,
                        onClick = { requestTabIndex = 2 },
                        checked = viewModel.requestHeaderEnabled,
                        onCheckedChange = { viewModel.requestHeaderEnabled = it },
                        content = { Text("HEADER") },
                    )
                    CheckableTab(
                        selected = requestTabIndex == 3,
                        onClick = { requestTabIndex = 3 },
                        checked = viewModel.requestAuthEnabled,
                        onCheckedChange = { viewModel.requestAuthEnabled = it },
                    ) {
                        var expanded by remember { mutableStateOf(false) }

                        Text(viewModel.requestAuthType.name)
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            for (type in RequestAuthType.values()) {
                                DropdownMenuItem(
                                    text = { Text(type.name) },
                                    onClick = { viewModel.requestAuthType = type; expanded = false },
                                )
                            }
                        }
                    }
                }
            }
            // Response tab.
            else if (tabIndex == 1) {
                var responseTabIndex by remember { mutableIntStateOf(0) }

                TabRow(selectedTabIndex = responseTabIndex) {
                    LeadingIconTab(
                        selected = responseTabIndex == 0,
                        text = { Text("BODY") },
                        icon = {},
                        onClick = { responseTabIndex = 0 }
                    )
                    LeadingIconTab(
                        selected = responseTabIndex == 1,
                        text = { Text("HEADER") },
                        icon = {},
                        onClick = { responseTabIndex = 1 }
                    )
                    LeadingIconTab(
                        selected = responseTabIndex == 2,
                        text = { Text("COOKIE") },
                        icon = {},
                        onClick = { responseTabIndex = 2 }
                    )
                }
            }
        }
    }
}

@Composable
private fun Protocol(protocol: ProtocolType, onSelected: (ProtocolType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    fun onItemClick(protocol: ProtocolType) {
        onSelected(protocol)
        expanded = false
    }

    TextButton(onClick = { expanded = true }) {
        Text(protocol.name)
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        ProtocolType.values().forEach { ProtocolItem(it, ::onItemClick) }
    }
}

@Composable
private fun ProtocolItem(type: ProtocolType, onClick: (ProtocolType) -> Unit) {
    DropdownMenuItem(text = { Text(type.name) }, onClick = { onClick(type) })
}

private val availableMethods = listOf(
    "GET", "POST", "PUT", "DELETE", "HEAD", "PATCH", "OPTIONS", "CUSTOM"
)

@Composable
private fun Method(method: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    TextButton(onClick = { expanded = true }) {
        Text(method)
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        availableMethods.forEach { method -> MethodItem(method) { onSelected(method); expanded = false } }
    }
}

@Composable
private fun MethodItem(name: String, onClick: (String) -> Unit) {
    DropdownMenuItem(text = { Text(name) }, onClick = { onClick(name) })
}
