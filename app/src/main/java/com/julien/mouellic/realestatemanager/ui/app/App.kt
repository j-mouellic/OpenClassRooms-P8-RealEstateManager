package com.julien.mouellic.realestatemanager.ui.app

import android.provider.Settings.Global.getString
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import com.julien.mouellic.realestatemanager.R
import androidx.navigation.compose.rememberNavController
import com.julien.mouellic.realestatemanager.ui.navigation.PropertyNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Real Estate Manager")
                },
                navigationIcon = {
                    IconButton(onClick = { /* Open Left Menu Future Usage */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Open Left Menu Future Usage")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        PropertyNavHost(navController = navController, modifier = Modifier.padding(paddingValues))
    }
}

data class NavigationItem(
    val longLabel: String,
    val shortLabel: String,
    val icon: ImageVector
)

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem("All properties", "All", Icons.Filled.Home),
        NavigationItem("Create Property", "Create", Icons.Filled.AddCircle),
        NavigationItem("Search Properties", "Search", Icons.Filled.Search),
        NavigationItem("Loan calculator", "Loan", Icons.Filled.Info)
    )

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp > 600

    BottomNavigation {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                BottomNavigationItem(
                    selected = false,
                    onClick = {
                        navController.navigate(item.longLabel.replace(" ", "_").lowercase())
                    },
                    label = {
                        val displayLabel = if (isTablet) item.longLabel else item.shortLabel
                        Text(displayLabel)
                    },
                    icon = {
                        Icon(item.icon, contentDescription = item.longLabel)
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigation(content: @Composable () -> Unit) {
    content()
}

@Composable
fun BottomNavigationItem(selected: Boolean, onClick: () -> Unit, label: @Composable () -> Unit, icon: @Composable () -> Unit) {
    TextButton(onClick = onClick) {
        icon()
        label()
    }
}