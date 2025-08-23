package com.julien.mouellic.realestatemanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.julien.mouellic.realestatemanager.ui.screen.allproperties.AllPropertiesScreen
import com.julien.mouellic.realestatemanager.ui.screen.createproperty.CreatePropertyScreen
import com.julien.mouellic.realestatemanager.ui.screen.detailedproperty.DetailedPropertyScreen
// import com.julien.mouellic.realestatemanager.ui.screen.allproperties.AllPropertiesScreen
// import com.julien.mouellic.realestatemanager.ui.screen.createproperty.CreatePropertyScreen
import com.julien.mouellic.realestatemanager.ui.screen.loancalculator.LoanCalculatorScreen
import com.julien.mouellic.realestatemanager.ui.screen.searchproperty.SearchPropertiesScreen


@Composable
fun PropertyNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(navController, startDestination = "search_properties", modifier = modifier) {
        // List & Map
        composable("all_properties") { AllPropertiesScreen(navController) }

        // Create property
        composable("create_property") { CreatePropertyScreen(null) }

        // Search property
        composable("search_properties") { SearchPropertiesScreen(navController) }

        // Loan Calculator
        composable("loan_calculator") { LoanCalculatorScreen() }

        // Property details
        composable("detailed_property/{propertyId}") { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId")?.toLong()
            if (propertyId != null) {
                DetailedPropertyScreen(propertyId)
            }
        }

        // Edit property
        composable("edit_property/{propertyId}") { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId")?.toLong()
            if (propertyId != null) {
                CreatePropertyScreen(propertyId)
            }
        }
    }
}