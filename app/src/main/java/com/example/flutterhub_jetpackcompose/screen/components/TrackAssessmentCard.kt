package com.example.flutterhub_jetpackcompose.screen.components

import android.content.Context
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flutterhub_jetpackcompose.data.models.AssessmentLink
import com.example.flutterhub_jetpackcompose.data.models.AssessmentModel
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

    Card(
        onClick = {
            navController.navigate("assessmentView/${link.id}")
        },
        shape = RoundedCornerShape(8.dp), // Rounded corners for the card
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp) // Card elevation (shadow effect)
    ) {

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
                    text = "Name: ${link.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )


                //Track button
                if (Hawk.get<Boolean?>("role").equals("admin")) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = link.checked,
                            onCheckedChange = { isChecked ->
//                                Toast.makeText(
//                                    context,
//                                    isChecked.toString(),
//                                    Toast.LENGTH_SHORT
//                                ).show()

                                link.checked = isChecked

                                viewModel.updateAssessmentLink(
                                    assessmentId = assessmentId,
                                    linkId = link.id,
                                    authName = link.id,
                                    checked = isChecked,
                                    onSuccess = {
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
                                containerColor = Color.Magenta.copy(
                                    alpha = 0.5f
                                )
                            ),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("View", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

