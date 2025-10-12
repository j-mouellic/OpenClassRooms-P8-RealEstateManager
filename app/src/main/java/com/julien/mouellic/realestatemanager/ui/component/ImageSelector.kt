package com.julien.mouellic.realestatemanager.ui.component

// ImageSelector.kt
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.julien.mouellic.realestatemanager.domain.model.Picture
import com.julien.mouellic.realestatemanager.ui.screen.createproperty.CreatePropertyUIState

@Composable
fun ImageSelector(
    uiState: CreatePropertyUIState.FormState,
    onDelete: (Picture) -> Unit,
    onMoveUp: (Picture) -> Unit,
    onMoveDown: (Picture) -> Unit
) {
    // --- Check if there are any pictures to display ---
    // If the list is empty, nothing is shown.
    if (uiState.pictures.isNotEmpty()) {

        // A vertical list of all selected pictures
        Column(modifier = Modifier.padding(vertical = 8.dp)) {

            // Loop through all pictures in the current form state
            uiState.pictures.forEach { picture ->

                // Only display the picture if it contains a valid thumbnail bitmap
                picture.thumbnailContent?.let { bitmap ->

                    // Each picture is displayed inside a horizontal row:
                    //  - The image on the left
                    //  - The control buttons (move up, move down, delete) on the right
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        // --- Image Preview ---
                        // Displays the picture's thumbnail.
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null, // No description (not needed here)
                            modifier = Modifier
                                .weight(1f)   // Takes most of the horizontal space
                                .height(200.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // --- Control Buttons ---
                        // A vertical column with 3 buttons:
                        //  + : Move image up in the list
                        //  - : Move image down
                        //  x : Delete image
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(60.dp)
                        ) {
                            // Move the picture up in the list
                            Button(onClick = { onMoveUp(picture) }) { Text("+") }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Move the picture down in the list
                            Button(onClick = { onMoveDown(picture) }) { Text("-") }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Delete the selected picture
                            Button(onClick = { onDelete(picture) }) { Text("x") }
                        }
                    }
                }
            }
        }
    }
}

