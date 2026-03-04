package com.example.ecocyclesolution.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecocyclesolution.viewModel.PickupViewModel
import com.example.ecocyclesolution.viewModel.UserViewModel

/* ===================================================== */
/* ================= USER DASHBOARD ==================== */
/* ===================================================== */

@Composable
fun UserDashboardScreen(
    navController: NavController,
    userVM: UserViewModel = viewModel(),
    pickupVM: PickupViewModel = viewModel()
) {

    /* ================= LOAD PICKUPS ONLY ================= */

    LaunchedEffect(Unit) {
        pickupVM.observeMyPickups()
    }

    /* ================= OBSERVE STATE ================= */

    val user = userVM.currentUser.value
    val pickups by pickupVM.pickupList
    val notification by pickupVM.message

    /* =====================================================
       ✅ WAIT UNTIL USER LOADS (CRASH FIX)
       ===================================================== */

    if (user == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    /* ================= SAFE DATA ================= */

    val activePickup =
        pickups.firstOrNull { it.status != "Completed" }

    val userName = user.fullName
    val ecoPoints = user.points
    val treesSaved = user.treesSaved
    val recycledKg =
        "%.1f".format(user.totalRecycledKg)

    /* ================= UI ================= */

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        /* HEADER */

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column {
                Text("Welcome back", color = Color.Gray)

                Text(
                    "Hello, $userName 👋",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color.White
                )

                if (notification.isNotEmpty()) {
                    Badge(
                        modifier =
                            Modifier.align(Alignment.TopEnd)
                    ) {}
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        /* ACTIVE PICKUP */

        activePickup?.let {

            Card(
                colors = CardDefaults.cardColors(
                    Color(0xFF1B5E20)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("tracking")
                    }
            ) {

                Column(Modifier.padding(16.dp)) {

                    Text(
                        "🚚 Pickup in Progress",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Status: ${it.status}",
                        color = Color.White
                    )

                    Text(
                        "Tap to Track",
                        color = Color.White.copy(.7f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
        }

        /* ECO CARD */

        Card(
            colors =
                CardDefaults.cardColors(Color(0xFF00C853)),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(Modifier.padding(20.dp)) {

                Text(
                    "Total Recycled",
                    color = Color.White
                )

                Text(
                    "$recycledKg kg",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )

                LinearProgressIndicator(
                    progress = {
                        (ecoPoints / 500f)
                            .coerceIn(0f, 1f)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Color.White
                )

                Text(
                    "$ecoPoints Eco Points",
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        /* STATS */

        Row(
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            StatCard(
                "Eco Points",
                ecoPoints.toString(),
                Modifier.weight(1f)
            )

            StatCard(
                "Trees Saved",
                treesSaved.toString(),
                Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(30.dp))

        Text(
            "Quick Actions",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            ActionCardIcon(
                "Schedule Pickup",
                "🚚",
                Modifier.weight(1f)
            ) {
                navController.navigate("schedule_pickup")
            }

            ActionCardIcon(
                "Track Pickup",
                "📍",
                Modifier.weight(1f)
            ) {
                navController.navigate("tracking")
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            ActionCardIcon(
                "Rewards",
                "🎁",
                Modifier.weight(1f)
            ) {
                navController.navigate("rewards")
            }

            ActionCardIcon(
                "Recycling Tips",
                "💡",
                Modifier.weight(1f)
            ) {
                navController.navigate("recyclingTips")
            }
        }

        Spacer(Modifier.height(80.dp))
    }
}
@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        modifier = modifier.height(120.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                title,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                value,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
fun ActionCardIcon(
    label: String,
    icon: String,
    modifier: Modifier,
    onClick: () -> Unit
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() }
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                icon,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                label,
                color = Color.White
            )
        }
    }
}