package ru.mironov.appwithmapsample.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import ru.mironov.appwithmapsample.app.navigation.NavGraph
import ru.mironov.appwithmapsample.core.ui.theme.AppWithMapSampleTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppWithMapSampleTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}