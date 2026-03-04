package com.example.ecocyclesolution.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecocyclesolution.viewModel.*

@Composable
fun RewardScreen(
    navController: NavController,
    rewardVM: RewardViewModel = viewModel(),
    userVM: UserViewModel = viewModel(),
    pickupVM: PickupViewModel = viewModel()
) {

    /* LOAD USER DATA */

    LaunchedEffect(Unit) {
        userVM.loadLoggedInUser()
    }

    val user by userVM.currentUser
    val ecoPoints = user?.points ?: 0

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {

        Text(
            "Rewards",
            color = Color.White,
            fontSize = 26.sp
        )

        Spacer(Modifier.height(10.dp))

        Text(
            "Your Points : $ecoPoints",
            color = Color(0xFF00E676),
            fontSize = 16.sp
        )

        Spacer(Modifier.height(20.dp))

        LazyColumn {

            items(rewardVM.rewards) { reward ->

                Card(
                    colors = CardDefaults.cardColors(
                        Color(0xFF1E1E1E)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                ) {

                    Column(
                        Modifier.padding(16.dp)
                    ) {

                        Text(
                            reward.title,
                            color = Color.White,
                            fontSize = 18.sp
                        )

                        Text(
                            reward.description,
                            color = Color.Gray
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "${reward.pointsRequired} Points",
                            color = Color(0xFF00C853)
                        )

                        Spacer(Modifier.height(10.dp))

                        Button(

                            enabled =
                                ecoPoints >= reward.pointsRequired,

                            onClick = {

                                userVM.redeemPoints(
                                    reward.pointsRequired
                                )

                                pickupVM.pushNotification(
                                    "Reward Redeemed 🎉",
                                    reward.title
                                )

                                userVM.loadLoggedInUser()
                            }
                        ) {

                            Text("Redeem")
                        }
                    }
                }
            }
        }
    }
}