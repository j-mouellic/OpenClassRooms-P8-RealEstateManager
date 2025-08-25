package com.julien.mouellic.realestatemanager.ui.app

import android.R.attr.fontWeight
import android.R.attr.tint
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.julien.mouellic.realestatemanager.R
import androidx.navigation.compose.rememberNavController
import com.julien.mouellic.realestatemanager.ui.navigation.PropertyNavHost
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent()
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Real Estate Manager")
                    },
                    navigationIcon = {
                        IconButton(onClick = {  }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "Open Left Menu Future Usage",
                                modifier = Modifier.clickable {
                                    scope.launch {
                                        drawerState.apply {
                                            if(isClosed) open() else {
                                                close()
                                                navController.navigate("all_properties")
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) { paddingValues ->
            PropertyNavHost(
                navController = navController,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun DrawerContent(modifier: Modifier = Modifier){
    Text(text= "Settings")
    HorizontalDivider()
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation{
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->

                val routeName = item.longLabel.replace(" ", "_").lowercase()
                val selected = currentRoute == routeName

                BottomNavigationItem(
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            navController.navigate(routeName) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    label = {
                        val displayLabel = if (isTablet) item.longLabel else item.shortLabel
                        Text(
                            text = displayLabel,
                            color = if (selected) Color.Black else Color.Gray,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = item.longLabel,
                            tint = if (selected) Color.Black else Color.Gray)
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