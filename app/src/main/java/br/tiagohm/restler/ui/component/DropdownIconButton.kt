package br.tiagohm.restler.ui.component

import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun <T> DropdownIconButton(
    icon: @Composable () -> Unit,
    items: Iterable<T?>,
    dropdownItemText: @Composable (T) -> Unit,
    dropdownItemIcon: @Composable ((T) -> Unit)? = null,
    onClick: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }, content = icon)
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        for (item in items) {
            if (item == null) Divider()
            else DropdownMenuItem(
                text = { dropdownItemText(item) },
                leadingIcon = { dropdownItemIcon?.invoke(item) },
                onClick = { onClick(item); expanded = false }
            )
        }
    }
}
