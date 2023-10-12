package br.tiagohm.restler.ui.theme

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import br.tiagohm.restler.R

@Composable
fun ResourceIcon(@DrawableRes id: Int) {
    Icon(painter = painterResource(id = id), contentDescription = null)
}

@Composable
fun TabPlusIcon() = ResourceIcon(id = R.drawable.tab_plus)

@Composable
fun TabIcon() = ResourceIcon(id = R.drawable.tab)

@Composable
fun BroomIcon() = ResourceIcon(id = R.drawable.broom)

@Composable
fun ContentSaveEditIcon() = ResourceIcon(id = R.drawable.content_save_edit)
