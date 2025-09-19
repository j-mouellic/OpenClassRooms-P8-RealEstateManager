package com.julien.mouellic.realestatemanager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.julien.mouellic.realestatemanager.ui.app.App
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = this
        setContent {
            App()
        }
    }

    companion object {
        lateinit var mainActivity: MainActivity
        private const val TAG = "MainActivity"
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    App()
}

/**
 * var locationPermissionsGranted by remember { mutableStateOf(areLocationPermissionsAlreadyGranted()) }
 *             var shouldShowPermissionRationale by remember {
 *                 mutableStateOf(
 *                     shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
 *                 )
 *             }
 *
 *             var shouldDirectUserToApplicationSettings by remember {
 *                 mutableStateOf(false)
 *             }
 *
 *             var currentPermissionsStatus by remember {
 *                 mutableStateOf(decideCurrentPermissionStatus(locationPermissionsGranted, shouldShowPermissionRationale))
 *             }
 *
 *             val locationPermissions = arrayOf(
 *                 Manifest.permission.ACCESS_FINE_LOCATION,
 *                 Manifest.permission.ACCESS_COARSE_LOCATION
 *             )
 * **/