@file:Suppress("NOTHING_TO_INLINE")

package br.tiagohm.restler.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import br.tiagohm.restler.R

@Composable
inline fun ResourceIcon(@DrawableRes id: Int) = Icon(painter = painterResource(id = id), contentDescription = null)

@Composable
inline fun ImageVectorIcon(imageVector: ImageVector) = Icon(imageVector = imageVector, contentDescription = null)

@Composable
inline fun TabPlusIcon() = ResourceIcon(id = R.drawable.tab_plus)

@Composable
inline fun TabIcon() = ResourceIcon(id = R.drawable.tab)

@Composable
inline fun BroomIcon() = ResourceIcon(id = R.drawable.broom)

@Composable
inline fun ContentSaveEditIcon() = ResourceIcon(id = R.drawable.content_save_edit)

@Composable
inline fun CallMadeIcon() = ResourceIcon(id = R.drawable.call_made)

@Composable
inline fun CallReceivedIcon() = ResourceIcon(id = R.drawable.call_received)

@Composable
inline fun FolderOpenIcon() = ResourceIcon(id = R.drawable.folder_open)

@Composable
inline fun FileDocumentIcon() = ResourceIcon(id = R.drawable.file_document)

@Composable
inline fun CheckIcon() = ImageVectorIcon(Icons.Filled.Check)

@Composable
inline fun CloseIcon() = ImageVectorIcon(Icons.Filled.Close)

@Composable
inline fun ArrowDropDownIcon() = ImageVectorIcon(Icons.Filled.ArrowDropDown)

@Composable
inline fun MenuIcon() = ImageVectorIcon(Icons.Filled.Menu)

@Composable
inline fun SendIcon() = ImageVectorIcon(Icons.Filled.Send)

@Composable
inline fun DeleteIcon() = ImageVectorIcon(Icons.Filled.Delete)

@Composable
inline fun SettingsIcon() = ImageVectorIcon(Icons.Filled.Settings)

@Composable
inline fun MoreVertIcon() = ImageVectorIcon(Icons.Filled.MoreVert)
