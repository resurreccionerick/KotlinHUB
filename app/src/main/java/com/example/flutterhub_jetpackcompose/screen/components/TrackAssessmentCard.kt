package com.example.flutterhub_jetpackcompose.screen.components

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.data.models.AssessmentLink
import com.example.flutterhub_jetpackcompose.viewmodel.AppViewModel
import com.orhanobut.hawk.Hawk

@Composable
fun TrackAssessmentCard(
    navController: NavController,
    link: AssessmentLink,
    context: Context,
    assessmentId: String,
    viewModel: AppViewModel,
) {

    Card(
        onClick = {
            navController.navigate("assessmentView/${assessmentId}")
        },
        shape = RoundedCornerShape(8.dp), // Rounded corners for the card
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp) // Card elevation (shadow effect)
    ) {
        // var checked by remember { mutableStateOf(link.checked) }
        // Box to align content inside the card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Padding inside the card
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically, // Center vertically
                horizontalArrangement = Arrangement.SpaceBetween // Spread text and buttons
            ) {
                Text(
                    text = "Name: ${link.field}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )


                //Track button
                if (Hawk.get<Boolean?>("role").equals("admin")) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Log.d("CHECKED IS: ", link.checked.toString());
                        Checkbox(
                            checked = link.checked,
                            onCheckedChange = { isChecked ->
                                link.checked = isChecked
//                                    checked = isChecked

                                // Update Firestore with the modified assessmentModel
                                viewModel.updateAssessmentLink(
                                    assessmentId = assessmentId,
                                    authName = link.field,
                                    checked = isChecked,
                                    onSuccess = {
                                        navController.popBackStack()
                                        Toast.makeText(
                                            context,
                                            "Updated",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onFailure = { msg ->
                                        Toast.makeText(
                                            context,
                                            msg,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        )

                        Button(
                            onClick = {
                                openTheLink(context, link.link)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onTertiaryContainer
                            ),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("View")
                        }
                    }
                }
            }
        }
    }
}

