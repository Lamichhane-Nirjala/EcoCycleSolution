package com.example.ecocyclesolution.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ecocyclesolution.view.ui.theme.EcoCycleSolutionTheme

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EcoCycleSolutionTheme {
                WelcomeScreen(onTimeout = {
                    // Navigate to SignupActivity after 3 seconds
                    val intent = Intent(this@WelcomeActivity, SignupActivity::class.java)
                    startActivity(intent)
                    finish() // Close WelcomeActivity so user can't go back
                })
            }
        }
    }
}