package com.example.kokokara_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kokokara_android.ui.AppNavHost
import com.example.kokokara_android.ui.theme.Kokokara_AndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kokokara_AndroidTheme {
                AppNavHost()
            }
        }
    }
}