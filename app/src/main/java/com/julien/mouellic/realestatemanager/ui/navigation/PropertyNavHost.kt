package com.julien.mouellic.realestatemanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.julien.mouellic.realestatemanager.ui.screen.allproperties.AllPropertiesViewModel
import com.julien.mouellic.realestatemanager.ui.screen.allproperties.list.AllPropertiesScreen
import com.julien.mouellic.realestatemanager.ui.screen.allproperties.search.SearchPropertiesScreen
import com.julien.mouellic.realestatemanager.ui.screen.createproperty.CreatePropertyScreen
import com.julien.mouellic.realestatemanager.ui.screen.detailedproperty.DetailedPropertyScreen
import com.julien.mouellic.realestatemanager.ui.screen.loancalculator.LoanCalculatorScreen


@Composable
fun PropertyNavHost(navController: NavHostController, modifier: Modifier) {

    val sharedViewModel: AllPropertiesViewModel = hiltViewModel()

    NavHost(navController, startDestination = "all_properties", modifier = modifier) {
        // Search property
        composable("search_properties") {
            SearchPropertiesScreen(navController, sharedViewModel)
        }

        // All properties
        composable("all_properties") {
            AllPropertiesScreen(navController, sharedViewModel)
        }

        // Create property
        composable("create_property") { CreatePropertyScreen(null) }

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