package com.example.flutterhub_jetpackcompose.screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun DifficultyDropDown(selectedDifficulty: String, onDifficultySelected: (String) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val difficulties = listOf("Basic", "Intermediate")

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedDifficulty,
            onValueChange = {},
            label = { Text("Difficulty") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Difficulty")
                }
            }
        )

        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            difficulties.forEach { difficulty ->
                DropdownMenuItem(
                    text = { Text(difficulty) },
                    onClick = {
                        onDifficultySelected(difficulty)
                        expanded = false

                    }
                )
            }
        }
    }
}