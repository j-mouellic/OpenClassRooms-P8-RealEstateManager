package com.julien.mouellic.realestatemanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
// import com.julien.mouellic.realestatemanager.ui.screen.allproperties.AllPropertiesScreen
// import com.julien.mouellic.realestatemanager.ui.screen.createproperty.CreatePropertyScreen
import com.julien.mouellic.realestatemanager.ui.screen.loancalculator.LoanCalculatorScreen
// import com.julien.mouellic.realestatemanager.ui.screen.searchproperties.SearchPropertiesScreen

@Composable
fun PropertyNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(navController, startDestination = "loan_calculator", modifier = modifier) {
       // composable("all_properties") { AllPropertiesScreen(navController) }
       // composable("create_property") { CreatePropertyScreen(null) }
       // composable("search_properties") { SearchPropertiesScreen() }
        composable("loan_calculator") { LoanCalculatorScreen() }
        /*composable("edit_property/{propertyId}") { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId")?.toLong()
            if (propertyId != null) {
                CreatePropertyScreen(propertyId)
            }
        }*/
    }
}