package com.example.ecocyclesolution.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecocyclesolution.viewModel.*
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay

@Composable
fun TrackingScreen(
    pickupVM: PickupViewModel,
    navController: NavController,
    userVM: UserViewModel = viewModel()
) {

    val pickups by pickupVM.pickupList
    val context = LocalContext.current

    var completing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        pickupVM.observeMyPickups()
        userVM.loadLoggedInUser()
    }

    val pickup =
        pickups.firstOrNull {
            it.status != "Completed"
        }

    if (pickup == null) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No Active Pickup")
        }
        return
    }

    /* AUTO STATUS FLOW */

    LaunchedEffect(pickup.pickupId, pickup.status) {

        when (pickup.status) {

            "Pending" -> {
                delay(8000)
                pickupVM.updatePickupStatus(
                    pickup.pickupId,
                    "Assigned"
                )
            }

            "Assigned" -> {
                delay(10000)
                pickupVM.updatePickupStatus(
                    pickup.pickupId,
                    "OnTheWay"
                )
            }

            "OnTheWay" -> {
                delay(12000)
                pickupVM.updatePickupStatus(
                    pickup.pickupId,
                    "PickedUp"
                )
            }
        }
    }

    Box(Modifier.fillMaxSize()) {

        PickupMapFull()

        Text(
            "Pickup Status",
            color = Color.White,
            fontSize = 22.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        )

        /* DRIVER CARD */

        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            colors =
                CardDefaults.cardColors(
                    Color(0xFF1B5E20)
                )
        ) {

            Row(
                Modifier.padding(16.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.Person,
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )

                Spacer(Modifier.width(12.dp))

                Column {

                    Text(
                        pickup.driverName ?: "Eco Driver",
                        color = Color.White
                    )

                    Text(
                        pickup.driverVehicle
                            ?: "Eco Truck",
                        color = Color.LightGray
                    )
                }

                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = {

                        pickup.driverPhone?.let {

                            val intent = Intent(
                                Intent.ACTION_DIAL,
                                Uri.parse("tel:$it")
                            )

                            context.startActivity(intent)
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.Call,
                        null,
                        tint = Color.White
                    )
                }
            }
        }

        /* BOTTOM PANEL */

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape =
                RoundedCornerShape(
                    topStart = 28.dp,
                    topEnd = 28.dp
                ),
            colors =
                CardDefaults.cardColors(
                    Color(0xFF121212)
                )
        ) {

            Column(Modifier.padding(20.dp)) {

                Text(
                    "Pickup Progress",
                    color = Color.White
                )

                Spacer(Modifier.height(12.dp))

                StatusItem(
                    "Request Received",
                    pickup.status != "Pending"
                )

                StatusItem(
                    "Driver Assigned",
                    pickup.status in listOf(
                        "Assigned",
                        "OnTheWay",
                        "PickedUp",
                        "Completed"
                    )
                )

                StatusItem(
                    "On The Way",
                    pickup.status in listOf(
                        "OnTheWay",
                        "PickedUp",
                        "Completed"
                    )
                )

                StatusItem(
                    "Picked Up",
                    pickup.status in listOf(
                        "PickedUp",
                        "Completed"
                    )
                )

                Spacer(Modifier.height(20.dp))

                /* COMPLETE PICKUP */

                if (pickup.status == "PickedUp") {

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !completing,
                        colors =
                            ButtonDefaults.buttonColors(
                                Color(0xFF00C853)
                            ),
                        onClick = {

                            completing = true

                            /* UPDATE STATUS */

                            pickupVM.updatePickupStatus(
                                pickup.pickupId,
                                "Completed"
                            )

                            /* ADD ECO POINTS */

                            userVM.addEcoPoints(
                                20,
                                2.5
                            )

                            pickupVM.pushNotification(
                                "Pickup Completed",
                                "Eco points added ✅"
                            )

                            /* WAIT FOR FIREBASE */

                            navController.navigate("dashboard") {
                                popUpTo("dashboard") { inclusive = true }
                            }
                        }
                    ) {
                        Text("Mark as Completed")
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row {

                    Button(
                        modifier =
                            Modifier.weight(1f),
                        onClick = {
                            navController.navigate(
                                "schedule_pickup?pickupId=${Uri.encode(pickup.pickupId)}"
                            )
                        }
                    ) {
                        Text("Edit")
                    }

                    Spacer(Modifier.width(10.dp))

                    Button(
                        modifier =
                            Modifier.weight(1f),
                        colors =
                            ButtonDefaults.buttonColors(
                                Color.Red
                            ),
                        onClick = {

                            pickupVM.deletePickup(
                                pickup.pickupId
                            )

                            navController.popBackStack()
                        }
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}

/* STATUS ITEM */

@Composable
fun StatusItem(title: String, done: Boolean) {

    Row(verticalAlignment = Alignment.CenterVertically) {

        Surface(
            modifier = Modifier.size(16.dp),
            shape = CircleShape,
            color =
                if (done)
                    Color(0xFF00C853)
                else Color.Gray
        ) {}

        Spacer(Modifier.width(10.dp))

        Text(
            title,
            color =
                if (done)
                    Color(0xFF00C853)
                else Color.Gray
        )
    }

    Spacer(Modifier.height(12.dp))
}

/* GOOGLE MAP */

@Composable
fun PickupMapFull() {

    val location =
        LatLng(27.6833, 84.4333)

    val cameraState =
        rememberCameraPositionState {
            position =
                CameraPosition.fromLatLngZoom(
                    location,
                    14f
                )
        }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraState
    ) {

        Marker(
            state =
                MarkerState(location),
            title = "Pickup Location"
        )
    }
}