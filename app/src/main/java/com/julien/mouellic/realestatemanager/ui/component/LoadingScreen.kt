package com.julien.mouellic.realestatemanager.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen() {
    // --- Main container ---
    // Box fills the entire screen, centers its content both vertically and horizontally.
    Box(
        modifier = Modifier
            .fillMaxSize()         // Takes all available space
            .padding(16.dp),        // Adds padding around the edges
        contentAlignment = Alignment.Center // Centers children content
    ) {
        // --- Inner column for vertical layout ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {

            // --- Circular loading spinner ---
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),  // Fixed size for the spinner
                strokeWidth = 4.dp,               // Thickness of the progress ring
                color = Color(0xFF000000)         // Black color for visibility
            )

            // --- Small space between spinner and text ---
            Spacer(modifier = Modifier.height(16.dp))

            // --- "Loading..." text label ---
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium // Slightly bold for emphasis
                ),
                color = MaterialTheme.colorScheme.onBackground // Adapts to theme
            )
        }
    }
}
