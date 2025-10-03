package com.julien.mouellic.realestatemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.julien.mouellic.realestatemanager.ui.app.App
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat

/**
 * MainActivity
 *
 * - Entry point of the application.
 * - Uses Jetpack Compose for UI via `setContent`.
 * - Handles runtime location permissions for GPS access.
 * - Integrates with Dagger Hilt for dependency injection (@AndroidEntryPoint).
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = this // Store global reference to MainActivity (for utilities or singleton use)

        setContent {

            // List of location permissions to request
            val locationPermissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            // State tracking whether location permissions have already been granted
            var locationPermissionsGranted by remember {
                mutableStateOf(areLocationPermissionsAlreadyGranted())
            }

            // State to show rationale if the user denied permission previously
            var shouldShowPermissionRationale by remember {
                mutableStateOf(
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }

            // Launcher to request permissions via Activity Result API
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                // Update states after user response
                locationPermissionsGranted = permissions.values.all { it }
                shouldShowPermissionRationale =
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
            }

            // Observe lifecycle to trigger permission request when activity starts
            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_START && !locationPermissionsGranted) {
                        // Launch permission request on activity start if not already granted
                        launcher.launch(locationPermissions)
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            // Start the main Compose App UI
            App()
        }
    }

    companion object {
        lateinit var mainActivity: MainActivity // Global reference to MainActivity
        private const val TAG = "MainActivity"
    }

    /**
     * Check if location permissions are already granted.
     */
    private fun areLocationPermissionsAlreadyGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}

/**
 * Preview of the App composable in Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    App()
}
