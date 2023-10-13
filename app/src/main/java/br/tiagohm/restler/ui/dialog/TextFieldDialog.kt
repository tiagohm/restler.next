package br.tiagohm.restler.ui.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun TextFieldDialog(
    initialValue: String,
    title: @Composable () -> Unit, label: @Composable () -> Unit,
    onConfirm: (String) -> Unit, onDismiss: () -> Unit,
    singleLine: Boolean = true,
) {
    var value by remember { mutableStateOf(initialValue) }
    var error by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = title,
        text = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = { value = it; error = value.isBlank() },
                label = label,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = singleLine,
                isError = error,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        },
        confirmButton = {
            Button(
                enabled = !error,
                onClick = { onConfirm(value) }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}
