package com.example.ecocyclesolution.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecocyclesolution.R
import com.example.ecocyclesolution.ui.theme.EcoCycleSolutionTheme
import com.example.ecocyclesolution.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()

        setContent {
            EcoCycleSolutionTheme {

                LoginScreen(

                    onSignupClick = {
                        startActivity(
                            Intent(this, SignupActivity::class.java)
                        )
                    },

                    onLoginSuccess = {
                        startActivity(
                            Intent(this, MainActivity::class.java)
                        )
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun LoginScreen(
    onSignupClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {

    val vm: UserViewModel = viewModel()
    val context = LocalContext.current

    val user by vm.currentUser
    val isLoading by vm.isLoading
    val error by vm.errorMessage

    var navigated by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    /* Navigate when user loads */

    LaunchedEffect(user) {
        if (user != null && !navigated) {
            navigated = true
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.rrrr),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 20.dp),
            contentScale = ContentScale.Fit
        )

        OutlinedTextField(
            value = vm.email.value,
            onValueChange = { vm.email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = vm.password.value,
            onValueChange = { vm.password.value = it },
            label = { Text("Password") },
            visualTransformation =
                if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        error?.let {
            Spacer(Modifier.height(6.dp))
            Text(it, color = Color.Red)
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Forgot Password?",
            color = Color.Green,
            modifier = Modifier
                .align(Alignment.End)
                .clickable {

                    context.startActivity(
                        Intent(
                            context,
                            ForgotPasswordActivity::class.java
                        )
                    )
                }
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { vm.login() },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {

            if (isLoading)
                CircularProgressIndicator(color = Color.White)
            else
                Text("Login")
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "Create Account",
            color = Color.Green,
            modifier = Modifier.clickable {
                onSignupClick()
            }
        )
    }
}