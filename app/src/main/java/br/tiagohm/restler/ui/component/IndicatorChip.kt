package br.tiagohm.restler.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.tiagohm.restler.ui.theme.White

@Composable
fun IndicatorChip(color: Color, text: String) {
    Surface(
        color = color,
        contentColor = White,
        shape = RoundedCornerShape(2.dp)
    ) {
        Box(
            modifier = Modifier.padding(2.dp, 2.dp),
        ) {
            Text(text)
        }
    }
}
