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
    if (uiState.pictures.isNotEmpty()) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            uiState.pictures.forEach { picture ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Image(
                        picture.thumbnailContent.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .height(200.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(60.dp)
                    ) {
                        Button(onClick = { onMoveUp(picture) }) {
                            Text("+")
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(onClick = { onMoveDown(picture) }) {
                            Text("-")
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(onClick = { onDelete(picture) }) {
                            Text("x")
                        }
                    }
                }
            }
        }
    }
}
