package com.example.flutterhub_jetpackcompose.screen.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnswerDropDown(
    selectedAns: String,
    choices: List<String>,
    onAnswerSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val answerOption = listOf("Choice 1", "Choice 2", "Choice 3", "Choice 4")

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedAns,
            onValueChange = {},
            label = { Text("Correct Answer") },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Blue),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Answer")
                }
            }
        )

        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false })
        {
            choices.forEach { answer ->
                DropdownMenuItem(
                    text = { Text(answer) },
                    onClick = {
                        onAnswerSelected(answer)
                        expanded = false
                    }
                )
            }
        }
    }
}