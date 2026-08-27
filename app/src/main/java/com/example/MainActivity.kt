package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.slant.ui.navigation.SlantNavHost
import com.slant.ui.theme.SlantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        setContent {
            SlantTheme(darkTheme = true) {
                SlantNavHost(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
