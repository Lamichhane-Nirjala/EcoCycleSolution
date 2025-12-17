package com.example.ecocyclesolution

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ecocyclesolution.ui.theme.EcoCycleSolutionTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EcoCycleSolutionTheme {

            }


            }
        }
    }

