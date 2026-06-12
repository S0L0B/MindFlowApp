package com.example.trabalhodenis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.trabalhodenis.ui.MainApp
import com.example.trabalhodenis.ui.theme.TrabalhoDenisTheme
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TrabalhoDenisTheme {
                MainApp()
            }
        }
    }
}