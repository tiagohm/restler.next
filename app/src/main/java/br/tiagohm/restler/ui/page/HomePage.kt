package br.tiagohm.restler.ui.page

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import br.tiagohm.restler.logic.enumeration.RequestAuthType
import br.tiagohm.restler.logic.enumeration.RequestBodyType
import br.tiagohm.restler.logic.enumeration.RequestMethodType
import br.tiagohm.restler.logic.enumeration.RequestProtocolType
import br.tiagohm.restler.logic.fileName
import br.tiagohm.restler.logic.fileSize
import br.tiagohm.restler.logic.lifecycle.HomeViewModel
import br.tiagohm.restler.ui.component.CheckableTab
import br.tiagohm.restler.ui.component.DropdownIconButton
import br.tiagohm.restler.ui.component.DropdownTextButton
import br.tiagohm.restler.ui.component.IndicatorChip
import br.tiagohm.restler.ui.dialog.TextFieldDialog
import br.tiagohm.restler.ui.theme.BroomIcon
import br.tiagohm.restler.ui.theme.ContentSaveEditIcon
import br.tiagohm.restler.ui.theme.FileDocumentIcon
import br.tiagohm.restler.ui.theme.FolderOpenIcon
import br.tiagohm.restler.ui.theme.Grey800
import br.tiagohm.restler.ui.theme.TabIcon
import br.tiagohm.restler.ui.theme.TabPlusIcon
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Suppress("RemoveExplicitTypeArguments")
private val moreOptions = listOf<Pair<String, @Composable () -> Unit>?>(
    "New tab" to { TabPlusIcon() },
    "Duplicate tab" to { TabPlusIcon() },
    "Restore tab" to { TabIcon() },
    null,
    "Clear" to { BroomIcon() },
    "Save as..." to { ContentSaveEditIcon() },
    null,
    "Settings" to { Icon(Icons.Filled.Settings, "Settings") },
)

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
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(Icons.Filled.Menu, "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Send, "Send")
                    }
                    DropdownIconButton(
                        icon = { Icon(Icons.Filled.MoreVert, "Options") },
                        items = moreOptions,
                        dropdownItemText = { Text(it.first) },
                        dropdownItemIcon = { it.second() },
                        onClick = {
                            if (it === moreOptions[4]) viewModel.clear()
                        },
                    )
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
                            ProtocolMenuButton(viewModel.protocol) { viewModel.protocol = it }

                            var openHttpMethodDialog by remember { mutableStateOf(false) }

                            MethodMenuButton(viewModel.method) {
                                if (it == RequestMethodType.CUSTOM) openHttpMethodDialog = true
                                else viewModel.method = it.name
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
                    text = { Text("REQUEST") },
                    onClick = { tabIndex = 0 }
                )
                Tab(
                    selected = tabIndex == 1,
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
                    },
                    onClick = { tabIndex = 1 }
                )
            }

            // Request tab.
            if (tabIndex == 0) {
                RequestTabRow(viewModel)

                if (requestTabIndex == 0) RequestBodyTab(viewModel)
            }
            // Response tab.
            else if (tabIndex == 1) {
                ResponseTabRow()
            }
        }
    }
}

@Composable
private fun ProtocolMenuButton(method: RequestProtocolType, onSelected: (RequestProtocolType) -> Unit) {
    DropdownTextButton(
        text = { Text(method.name) },
        items = RequestProtocolType.values().toList(),
        dropdownItemText = { Text(it.name) },
        onClick = onSelected,
    )
}

@Composable
private fun MethodMenuButton(method: String, onSelected: (RequestMethodType) -> Unit) {
    DropdownTextButton(
        text = { Text(method) },
        items = RequestMethodType.values().toList(),
        dropdownItemText = { Text(it.name) },
        onClick = onSelected,
    )
}

// Request.

var requestTabIndex by mutableIntStateOf(0)

@Composable
private fun RequestTabRow(viewModel: HomeViewModel) {
    ScrollableTabRow(selectedTabIndex = requestTabIndex, edgePadding = 0.dp) {
        CheckableTab(
            selected = requestTabIndex == 0,
            checked = viewModel.requestBodyEnabled,
            onCheckedChange = { viewModel.requestBodyEnabled = it },
            onClick = { requestTabIndex = 0 }
        ) {
            Text(viewModel.requestBodyType.name)
            DropdownIconButton(
                icon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                items = RequestBodyType.values().toList(),
                dropdownItemText = { Text(it.name) },
                onClick = { viewModel.requestBodyType = it }
            )
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
            Text(viewModel.requestAuthType.name)
            DropdownIconButton(
                icon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                items = RequestAuthType.values().toList(),
                dropdownItemText = { Text(it.name) },
                onClick = { viewModel.requestAuthType = it }
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
                Spacer(Modifier.width(4.dp))
                if (fileUri != null) {
                    val contentResolver = viewModel.appContext.contentResolver
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
            val readFilePermission = rememberPermissionState(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )

            Button(onClick = {
                if (Build.VERSION.SDK_INT <= 32) {
                    if (readFilePermission.status.isGranted) {
                        openDocumentLauncher.launch("*/*")
                    } else {
                        readFilePermission.launchPermissionRequest()
                    }
                } else {
                    openDocumentLauncher.launch("*/*")
                }
            }) {
                FolderOpenIcon()
                Text("Open file")
            }
            Spacer(Modifier.width(4.dp))
            Button(onClick = { viewModel.requestBodyFile = null }) {
                Icon(Icons.Filled.Delete, contentDescription = null)
                Text("Remove")
            }
        }
    }
}

// Response.

var responseTabIndex by mutableIntStateOf(0)

@Composable
private fun ResponseTabRow() {
    TabRow(selectedTabIndex = responseTabIndex) {
        Tab(
            selected = responseTabIndex == 0,
            text = { Text("BODY") },
            onClick = { responseTabIndex = 0 }
        )
        Tab(
            selected = responseTabIndex == 1,
            text = { Text("HEADER") },
            onClick = { responseTabIndex = 1 }
        )
        Tab(
            selected = responseTabIndex == 2,
            text = { Text("COOKIE") },
            onClick = { responseTabIndex = 2 }
        )
    }
}
