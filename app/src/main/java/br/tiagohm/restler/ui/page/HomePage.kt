package br.tiagohm.restler.ui.page

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.tiagohm.restler.logic.enumeration.RequestAuthType
import br.tiagohm.restler.logic.enumeration.RequestBodyType
import br.tiagohm.restler.logic.enumeration.RequestMethodType
import br.tiagohm.restler.logic.enumeration.RequestProtocolType
import br.tiagohm.restler.logic.fileName
import br.tiagohm.restler.logic.fileSize
import br.tiagohm.restler.logic.lifecycle.HomeViewModel
import br.tiagohm.restler.ui.component.CheckableTab
import br.tiagohm.restler.ui.component.IconSpacer
import br.tiagohm.restler.ui.component.IndicatorChip
import br.tiagohm.restler.ui.dialog.TextFieldDialog
import br.tiagohm.restler.ui.theme.ArrowDropDownIcon
import br.tiagohm.restler.ui.theme.BroomIcon
import br.tiagohm.restler.ui.theme.ContentSaveEditIcon
import br.tiagohm.restler.ui.theme.DeleteIcon
import br.tiagohm.restler.ui.theme.FileDocumentIcon
import br.tiagohm.restler.ui.theme.FolderOpenIcon
import br.tiagohm.restler.ui.theme.Grey800
import br.tiagohm.restler.ui.theme.MenuIcon
import br.tiagohm.restler.ui.theme.MoreVertIcon
import br.tiagohm.restler.ui.theme.SendIcon
import br.tiagohm.restler.ui.theme.SettingsIcon
import br.tiagohm.restler.ui.theme.TabIcon
import br.tiagohm.restler.ui.theme.TabPlusIcon
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

var requestTabIndex by mutableIntStateOf(0)
var responseTabIndex by mutableIntStateOf(0)

@Composable
fun HomePage(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigationIconClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Home")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) { MenuIcon() }
                },
                actions = {
                    IconButton(onClick = { }) { SendIcon() }
                    MoreOptionsDropdownButton(viewModel)
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
                            ProtocolDropdownButton(viewModel)
                            MethodDropdownButton(viewModel)
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
                Tab(
                    selected = tabIndex == 0,
                    onClick = { tabIndex = 0 },
                    text = { Text("REQUEST") }
                )
                Tab(
                    selected = tabIndex == 1,
                    onClick = { tabIndex = 1 },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("RESPONSE")
                            Row {
                                IndicatorChip(color = Grey800, text = "${viewModel.responseStatus}")
                                Spacer(Modifier.width(2.dp))
                                IndicatorChip(color = Grey800, text = "${viewModel.responseTime} ms")
                                Spacer(Modifier.width(2.dp))
                                IndicatorChip(color = Grey800, text = "${viewModel.responseLength} B")
                            }
                        }
                    }
                )
            }

            // Request tab.
            if (tabIndex == 0) {
                ScrollableTabRow(selectedTabIndex = requestTabIndex, edgePadding = 0.dp) {
                    RequestTab(
                        tabIndex = 0,
                        checked = viewModel.requestBodyEnabled,
                        onCheckedChange = { viewModel.requestBodyEnabled = it },
                    ) {
                        Text(viewModel.requestBodyType.name)
                        RequestBodyDropdownButton(onTypeChange = { viewModel.requestBodyType = it })
                    }
                    RequestTab(
                        tabIndex = 1,
                        checked = viewModel.requestQueryEnabled,
                        onCheckedChange = { viewModel.requestQueryEnabled = it },
                    ) {
                        Text("QUERY")
                    }
                    RequestTab(
                        tabIndex = 2,
                        checked = viewModel.requestHeaderEnabled,
                        onCheckedChange = { viewModel.requestHeaderEnabled = it },
                    ) {
                        Text("HEADER")
                    }
                    RequestTab(
                        tabIndex = 3,
                        checked = viewModel.requestAuthEnabled,
                        onCheckedChange = { viewModel.requestAuthEnabled = it },
                    ) {
                        Text(viewModel.requestAuthType.name)
                        RequestAuthDropdownButton(onTypeChange = { viewModel.requestAuthType = it })
                    }
                }

                if (requestTabIndex == 0) RequestBodyTab(viewModel)
            }
            // Response tab.
            else if (tabIndex == 1) {
                TabRow(selectedTabIndex = responseTabIndex) {
                    ResponseTab(
                        tabIndex = 0,
                        text = { Text("BODY") }
                    )
                    ResponseTab(
                        tabIndex = 1,
                        text = { Text("HEADER") }
                    )
                    ResponseTab(
                        tabIndex = 2,
                        text = { Text("COOKIE") }
                    )
                }
            }
        }
    }
}

@Composable
private fun MoreOptionsDropdownButton(viewModel: HomeViewModel) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) { MoreVertIcon() }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(
            text = { Text("New tab") },
            leadingIcon = { TabPlusIcon() },
            onClick = { expanded = false }
        )
        DropdownMenuItem(
            text = { Text("Duplicate tab") },
            leadingIcon = { TabPlusIcon() },
            onClick = { expanded = false }
        )
        DropdownMenuItem(
            text = { Text("Restore tab") },
            leadingIcon = { TabIcon() },
            onClick = { expanded = false }
        )
        Divider()
        DropdownMenuItem(
            text = { Text("Clear") },
            leadingIcon = { BroomIcon() },
            onClick = { viewModel.clear(); expanded = false }
        )
        DropdownMenuItem(
            text = { Text("Save as...") },
            leadingIcon = { ContentSaveEditIcon() },
            onClick = { expanded = false }
        )
        Divider()
        DropdownMenuItem(
            text = { Text("Settings") },
            leadingIcon = { SettingsIcon() },
            onClick = { expanded = false }
        )
    }
}

@Composable
@Suppress("NOTHING_TO_INLINE")
private inline fun ProtocolDropdownButton(viewModel: HomeViewModel) {
    var expanded by remember { mutableStateOf(false) }

    TextButton(onClick = { expanded = true }) { Text(viewModel.protocol.name) }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        for (item in RequestProtocolType.values()) {
            DropdownMenuItem(
                text = { Text(item.name) },
                onClick = { viewModel.protocol = item; expanded = false }
            )
        }
    }
}

@Composable
@Suppress("NOTHING_TO_INLINE")
private inline fun MethodDropdownButton(viewModel: HomeViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var openHttpMethodDialog by remember { mutableStateOf(false) }

    TextButton(onClick = { expanded = true }) { Text(viewModel.method) }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        for (item in RequestMethodType.values()) {
            DropdownMenuItem(
                text = { Text(item.name) },
                onClick = {
                    if (item == RequestMethodType.CUSTOM) openHttpMethodDialog = true
                    else viewModel.method = item.name
                    expanded = false
                }
            )
        }
    }

    if (openHttpMethodDialog) {
        TextFieldDialog(
            initialValue = viewModel.customMethod,
            title = { Text("HTTP Method") },
            label = { Text("Name") },
            onConfirm = { viewModel.customMethod = it.trim(); openHttpMethodDialog = false },
            onDismiss = { openHttpMethodDialog = false }
        )
    }
}

@Composable
private fun RequestTab(
    tabIndex: Int,
    checked: Boolean, onCheckedChange: (Boolean) -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    CheckableTab(
        selected = requestTabIndex == tabIndex,
        checked = checked,
        onCheckedChange = onCheckedChange,
        onClick = { requestTabIndex = tabIndex },
        content = content,
    )
}

@Composable
private fun RequestBodyDropdownButton(onTypeChange: (RequestBodyType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) { ArrowDropDownIcon() }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        for (item in RequestBodyType.values()) {
            DropdownMenuItem(
                text = { Text(item.name) },
                onClick = { onTypeChange(item); expanded = false }
            )
        }
    }
}

@Composable
private fun RequestAuthDropdownButton(onTypeChange: (RequestAuthType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) { ArrowDropDownIcon() }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        for (item in RequestAuthType.values()) {
            DropdownMenuItem(
                text = { Text(item.name) },
                onClick = { onTypeChange(item); expanded = false }
            )
        }
    }
}

@Composable
private fun RequestBodyTab(viewModel: HomeViewModel) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        when (viewModel.requestBodyType) {
            RequestBodyType.TEXT -> Unit
            RequestBodyType.FORM -> Unit
            RequestBodyType.MULTIPART -> Unit
            RequestBodyType.FILE -> RequestFileBodyTab(viewModel)
            RequestBodyType.NONE -> Unit
        }
    }
}

@Composable
private fun RequestFileBodyTab(viewModel: HomeViewModel) {
    val openDocumentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        viewModel.requestBodyFile = it ?: return@rememberLauncherForActivityResult
    }

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val fileUri = viewModel.requestBodyFile

        Surface(tonalElevation = 1.dp) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                FileDocumentIcon()
                IconSpacer()
                if (fileUri != null) {
                    val context = LocalContext.current
                    val contentResolver = context.contentResolver
                    val fileName = contentResolver.fileName(fileUri) ?: "$fileUri"
                    val fileSize = contentResolver.fileSize(fileUri) ?: "? B"
                    Text("$fileName ($fileSize B)")
                } else {
                    Text("No file selected")
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val readFilePermission = rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)

            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT <= 32) {
                        if (readFilePermission.status.isGranted) {
                            openDocumentLauncher.launch("*/*")
                        } else {
                            readFilePermission.launchPermissionRequest()
                        }
                    } else {
                        openDocumentLauncher.launch("*/*")
                    }
                },
            ) {
                FolderOpenIcon()
                IconSpacer()
                Text("Open file")
            }
            IconSpacer()
            Button(onClick = { viewModel.requestBodyFile = null }) {
                DeleteIcon()
                IconSpacer()
                Text("Remove")
            }
        }
    }
}

@Composable
@Suppress("NOTHING_TO_INLINE")
private inline fun ResponseTab(
    tabIndex: Int,
    noinline text: @Composable () -> Unit,
) {
    Tab(
        selected = responseTabIndex == tabIndex,
        onClick = { responseTabIndex = tabIndex },
        text = text,
    )
}
