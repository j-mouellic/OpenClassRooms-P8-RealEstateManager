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