package com.julien.mouellic.realestatemanager.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.julien.mouellic.realestatemanager.domain.model.Agent

@Composable
fun SelectAgentField(
    allAgents: List<Agent>,             // List of all available agents (data from DB or repository)
    selectedAgent: Agent?,              // Currently selected agent (if any)
    onAgentSelected: (Agent) -> Unit    // Callback triggered when an agent is chosen
) {
    // --- UI State management ---
    var isDropdownOpen by remember { mutableStateOf(false) }   // Controls whether the dropdown is visible
    var textFieldValue by remember {
        mutableStateOf(
            selectedAgent?.let { it.lastName + " " + it.firstName } ?: "Select..." // Displayed agent name or placeholder
        )
    }

    // --- UI Layout container ---
    Column {
        // --- Field label ---
        Text("Select Agent *")  // Label above the dropdown

        Spacer(modifier = Modifier.height(8.dp))

        // --- Read-only text field acting as dropdown trigger ---
        OutlinedTextField(
            value = textFieldValue,       // Shows selected agent name or placeholder
            onValueChange = { newText ->  // Not editable, but must define handler
                textFieldValue = newText
            },
            label = { Text("Agent") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,              // Prevent manual text input
            trailingIcon = {              // Dropdown icon on the right
                IconButton(onClick = { isDropdownOpen = !isDropdownOpen }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )

        // --- Dropdown menu showing list of agents ---
        DropdownMenu(
            expanded = isDropdownOpen,             // Visibility controlled by state
            onDismissRequest = { isDropdownOpen = false }, // Close when clicked outside
            modifier = Modifier.fillMaxWidth()
        ) {
            allAgents.forEach { agent ->
                DropdownMenuItem(
                    onClick = {
                        // Update selected agent
                        onAgentSelected(agent)
                        textFieldValue = agent.lastName + " " + agent.firstName
                        isDropdownOpen = false  // Close dropdown after selection
                    },
                    text = { Text(agent.lastName + " " + agent.firstName) } // Display full name
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Display currently selected agent ---
        if (selectedAgent != null) {
            Text("Selected Agent: ${selectedAgent.firstName + " " + selectedAgent.lastName}")
        }
    }
}
