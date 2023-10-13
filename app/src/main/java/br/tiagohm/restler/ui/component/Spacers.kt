package br.tiagohm.restler.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
@Suppress("NOTHING_TO_INLINE")
inline fun IconSpacer() {
    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
}
