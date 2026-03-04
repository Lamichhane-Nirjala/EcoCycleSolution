//package com.example.ecocyclesolution.view
//
//import android.content.Intent
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.ecocyclesolution.R
//
//@Composable
//fun OnboardingScreen() {
//    val context = LocalContext.current
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF8FAF3))
//            .padding(horizontal = 24.dp, vertical = 32.dp)
//    ) {
//
//        // Skip Button Top-Right
//        TextButton(
//            onClick = {
//                val intent = Intent(context, SignInActivity::class.java)
//                context.startActivity(intent)
//            },
//            modifier = Modifier.align(Alignment.TopEnd)
//        ) {
//            Text(
//                text = "Skip",
//                color = Color(0xFF2E7D32),
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Medium
//            )
//        }
//
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//
//            // Image Card with shadow and rounded corners
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(280.dp)
//                    .clip(RoundedCornerShape(20.dp))
//                    .shadow(8.dp, RoundedCornerShape(20.dp))
//                    .background(Color.White),
//                contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.onboarding),
//                    contentDescription = "Recycling Illustration",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clip(RoundedCornerShape(20.dp))
//                )
//            }
//
//            Spacer(modifier = Modifier.height(28.dp))
//
//            // Title
//            Text(
//                text = "Recycling Made Simple",
//                fontSize = 26.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFF2E7D32),
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            // Description
//            Text(
//                text = "EcoCycle makes it easy to separate waste, track your impact, and contribute to a greener planet every day.",
//                fontSize = 15.sp,
//                color = Color(0xFF4A4A4A),
//                textAlign = TextAlign.Center,
//                lineHeight = 22.sp
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Progress Dots
//            Row(
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(10.dp)
//                        .background(Color(0xFF2E7D32), RoundedCornerShape(5.dp))
//                )
//                Spacer(modifier = Modifier.width(6.dp))
//                Box(
//                    modifier = Modifier
//                        .size(10.dp)
//                        .background(Color(0xFFBDBDBD), RoundedCornerShape(5.dp))
//                )
//                Spacer(modifier = Modifier.width(6.dp))
//                Box(
//                    modifier = Modifier
//                        .size(10.dp)
//                        .background(Color(0xFFBDBDBD), RoundedCornerShape(5.dp))
//                )
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // Next Button
//            Button(
//                onClick = {
//                    val intent = Intent(context, SignInActivity::class.java)
//                    context.startActivity(intent)
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
//                shape = RoundedCornerShape(16.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(52.dp)
//            ) {
//                Text(
//                    text = "Next →",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                )
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewOnboardingScreen() {
//    OnboardingScreen()
//}