package com.example.flutterhub_jetpackcompose.screen.components.card

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.data.models.AssessmentLink
import com.example.flutterhub_jetpackcompose.screen.components.screen.openTheLink
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@Composable
fun TrackAssessmentCard(
    navController: NavController,
    link: AssessmentLink,
    viewModel: AppViewModel,
    context: Context,
    assessmentId: String
) {
    var comment by rememberSaveable { mutableStateOf(link.comment) }
    var boolChecked by rememberSaveable { mutableStateOf("") }
    Card(
        onClick = {
            if (!Hawk.get<Boolean?>("role").equals("admin")) {
                navController.navigate("assessmentView/${link.id}")
            }
        },
        shape = RoundedCornerShape(8.dp), // Rounded corners for the card
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Padding inside the card,
        elevation = CardDefaults.cardElevation(4.dp) // Card elevation (shadow effect)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Padding inside the card
        ) {

            if (Hawk.get<Boolean?>("role").equals("admin")) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Name: ${link.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Button(
                            onClick = {
                                openTheLink(navController,link.link)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Magenta.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("View", color = Color.White)
                        }
                    }

                    // RadioGroup-like behavior
                    val selectedOption = remember {
                        mutableStateOf(
                            if (link.checked == "true") {
                                "Approved"
                            } else if (link.checked == "false") {
                                "Decline"
                            } else {
                                "null"
                            }
                        )
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedOption.value == "Approved",
                                onClick = {
                                    selectedOption.value = "Approved"
                                    link.checked = "true"
                                }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                "Approved",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedOption.value == "Decline",
                                onClick = {
                                    selectedOption.value = "Decline"
                                    link.checked = "false"
                                }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                "Decline",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        OutlinedTextField(
                            value = comment!!,
                            maxLines = 1,
                            onValueChange = { comment = it },
                            label = {
                                Text(
                                    "Add Comment Here"
                                )
                            }
                        )



                        IconButton(onClick = {
                            if (link.checked.equals("true")) {
                                boolChecked = "true"
                            } else if (link.checked.equals("false")) {
                                boolChecked = "false"
                            } else {
                                boolChecked = "null"
                            }
                            if (comment!!.isNotEmpty()) {
                                viewModel.updateAssessmentLink(
                                    assessmentId = assessmentId,
                                    linkId = link.id,
                                    authID = link.id, // Ensure this is correct or replace with the right value
                                    checked = boolChecked,
                                    comment = comment!!,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Updated",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.popBackStack()
                                    },
                                    onFailure = { msg ->
                                        // Revert UI state if update fails
                                        selectedOption.value = "Approved"
                                        Toast.makeText(
                                            context,
                                            msg,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            } else {
                                Toast.makeText(context, "Enter a comment", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                tint = Color(0xFF039be5)
                            )
                        }
                    }
                }
            }

        }
    }
}

