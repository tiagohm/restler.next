package br.tiagohm.restler.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CheckableTab(
    selected: Boolean,
    checked: Boolean,
    onClick: () -> Unit,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Tab(selected = selected, onClick = onClick, enabled = enabled) {
        Row(
            modifier = Modifier.padding(16.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
            content()
        }
    }
}
