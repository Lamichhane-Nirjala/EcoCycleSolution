package com.example.ecocyclesolution.view

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                ForgotPasswordScreen()
            }
        }
    }
}


@Composable
fun ForgotPasswordScreen() {

    val context = LocalContext.current
    val activity = context as Activity
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var linkSent by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF031B12),
                        Color(0xFF071F16)
                    )
                )
            )
            .padding(24.dp)
    ) {

        Column {

            Spacer(Modifier.height(20.dp))

            /* ===== BACK BUTTON ===== */

            IconButton(
                onClick = { activity.finish() }
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(Modifier.height(20.dp))

            /* ===== TITLE ===== */

            Text(
                "Forgot\nPassword?",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Don't worry! Enter your email and we'll send you a reset link.",
                color = Color.Gray
            )

            Spacer(Modifier.height(40.dp))

            Text(
                "Email Address",
                color = Color.White
            )

            Spacer(Modifier.height(10.dp))

            /* ===== EMAIL FIELD ===== */

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        null,
                        tint = Color(0xFF00C853)
                    )
                },
                trailingIcon = {
                    if (email.contains("@"))
                        Icon(
                            Icons.Default.CheckCircle,
                            null,
                            tint = Color(0xFF00C853)
                        )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00C853),
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                )
            )

            Spacer(Modifier.height(40.dp))

            /* ===== RESET BUTTON ===== */

            Button(
                onClick = {

                    if (email.isBlank()) {
                        Toast.makeText(
                            context,
                            "Enter email first",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    loading = true

                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->

                            loading = false

                            if (task.isSuccessful) {
                                linkSent = true
                            } else {
                                Toast.makeText(
                                    context,
                                    task.exception?.message
                                        ?: "Error sending email",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                },

                enabled = !loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC107)
                )
            ) {

                if (loading) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Send Reset Link →",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Need help? Contact Support",
                color = Color.Gray,
                modifier =
                    Modifier.align(
                        Alignment.CenterHorizontally
                    )
            )
        }

        /* ===== SUCCESS CARD ===== */

        if (linkSent) {

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.CheckCircle,
                        null,
                        tint = Color(0xFF00C853)
                    )

                    Spacer(Modifier.width(12.dp))

                    Column {

                        Text(
                            "Link Sent!",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Check your inbox to recover password."
                        )
                    }
                }
            }
        }
    }
}