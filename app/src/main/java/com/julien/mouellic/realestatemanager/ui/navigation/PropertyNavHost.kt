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

/**
 * PropertyNavHost
 *
 * Purpose:
 *  Defines the navigation graph for the app using Jetpack Compose Navigation.
 *  Routes between screens related to properties, property creation, search, details, and loan calculator.
 *
 * Parameters:
 *  - navController: Controller to manage navigation between composables
 *  - modifier: Optional UI modifier
 */
@Composable
fun PropertyNavHost(navController: NavHostController, modifier: Modifier) {

    // Shared ViewModel for the properties list and search screens
    val sharedViewModel: AllPropertiesViewModel = hiltViewModel()

    NavHost(
        navController,
        startDestination = "all_properties",  // Default screen at app launch
        modifier = modifier
    ) {

        // ------------------------------------------------------
        // Search Properties Screen
        // ------------------------------------------------------
        composable("search_properties") {
            // Displays search filters and results
            SearchPropertiesScreen(navController, sharedViewModel)
        }

        // ------------------------------------------------------
        // All Properties Screen
        // ------------------------------------------------------
        composable("all_properties") {
            // Main listing of all properties
            AllPropertiesScreen(navController, sharedViewModel)
        }

        // ------------------------------------------------------
        // Create Property Screen
        // ------------------------------------------------------
        composable("create_property") {
            // Screen to add a new property
            CreatePropertyScreen(null)
        }

        // ------------------------------------------------------
        // Loan Calculator Screen
        // ------------------------------------------------------
        composable("loan_calculator") {
            // Screen to calculate mortgage/loan monthly payments
            LoanCalculatorScreen()
        }

        // ------------------------------------------------------
        // Property Details Screen
        // ------------------------------------------------------
        composable("detailed_property/{propertyId}") { backStackEntry ->
            // Extract propertyId from navigation arguments
            val propertyId = backStackEntry.arguments?.getString("propertyId")?.toLong()
            if (propertyId != null) {
                DetailedPropertyScreen(propertyId)
            }
        }

        // ------------------------------------------------------
        // Edit Property Screen
        // ------------------------------------------------------
        composable("edit_property/{propertyId}") { backStackEntry ->
            // Extract propertyId and open the create/edit property form prefilled
            val propertyId = backStackEntry.arguments?.getString("propertyId")?.toLong()
            if (propertyId != null) {
                CreatePropertyScreen(propertyId)
            }
        }
    }
}
