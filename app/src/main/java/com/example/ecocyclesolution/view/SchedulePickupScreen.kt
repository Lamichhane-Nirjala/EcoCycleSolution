package com.example.ecocyclesolution.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecocyclesolution.viewModel.PickupViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulePickupScreen(
    navController: NavController,
    pickupId: String? = null,
    pickupVM: PickupViewModel = viewModel()
) {

    val pickups = pickupVM.pickupList.value

    val activePickup =
        pickups.firstOrNull { it.status != "Completed" }

    val editingPickup =
        pickups.find { it.pickupId == pickupId }

    var waste by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember {
        mutableStateOf("10:00 AM - 12:00 PM")
    }

    /* LOAD EDIT DATA */

    LaunchedEffect(editingPickup) {

        editingPickup?.let {

            waste = it.wasteType
            location = it.location
            selectedDate = LocalDate.parse(it.date)
            selectedTime = it.time
        }
    }

    val dates =
        (0..30).map {
            LocalDate.now().plusDays(it.toLong())
        }

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)

    ) {

        /* HEADER */

        item {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )

                Spacer(Modifier.width(12.dp))

                Text(

                    if (pickupId == null)
                        "Schedule Pickup"
                    else
                        "Edit Pickup",

                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        /* WASTE TYPE */

        item {

            Spacer(Modifier.height(20.dp))

            val wasteList =
                listOf(
                    "Plastic",
                    "Paper",
                    "Metal",
                    "Organic"
                )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(240.dp),
                userScrollEnabled = false
            ) {

                items(wasteList) { item ->

                    Card(
                        modifier = Modifier
                            .padding(6.dp)
                            .clickable {
                                waste = item
                            },

                        colors =
                            CardDefaults.cardColors(
                                if (waste == item)
                                    Color(0xFF00C853)
                                else
                                    Color(0xFF1E1E1E)
                            )
                    ) {

                        Box(
                            Modifier.padding(20.dp),
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                item,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        /* DATE */

        item {

            Spacer(Modifier.height(20.dp))

            LazyRow {

                items(dates) { date ->

                    Card(

                        modifier = Modifier
                            .padding(6.dp)
                            .size(70.dp)
                            .clickable {
                                selectedDate = date
                            },

                        colors =
                            CardDefaults.cardColors(
                                if (selectedDate == date)
                                    Color(0xFF00C853)
                                else
                                    Color(0xFF1E1E1E)
                            )
                    ) {

                        Column(
                            Modifier.fillMaxSize(),
                            horizontalAlignment =
                                Alignment.CenterHorizontally,
                            verticalArrangement =
                                Arrangement.Center
                        ) {

                            Text(
                                date.dayOfWeek.getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.getDefault()
                                ),
                                color = Color.White
                            )

                            Text(
                                date.dayOfMonth.toString(),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        /* LOCATION */

        item {

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = location,
                onValueChange = {
                    location = it
                },
                label = {
                    Text("Pickup Address")
                },
                modifier =
                    Modifier.fillMaxWidth()
            )
        }

        /* SAVE BUTTON */

        item {

            Spacer(Modifier.height(30.dp))

            Button(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),

                colors =
                    ButtonDefaults.buttonColors(
                        Color(0xFF00C853)
                    ),

                onClick = {

                    if (waste.isBlank() || location.isBlank()) {
                        pickupVM.pushNotification(
                            "Missing Info",
                            "Please select waste type and location"
                        )
                        return@Button
                    }

                    if (
                        pickupId == null &&
                        activePickup != null
                    ) {

                        pickupVM.pushNotification(
                            "Pickup Exists",
                            "Complete existing pickup first"
                        )

                        return@Button
                    }

                    if (pickupId == null) {

                        pickupVM.schedulePickup(
                            waste,
                            selectedDate.toString(),
                            selectedTime,
                            location
                        )

                    } else {

                        pickupVM.updatePickup(
                            pickupId,
                            waste,
                            selectedDate.toString(),
                            selectedTime,
                            location
                        )
                    }

                    navController.navigate("tracking") {
                        launchSingleTop = true
                    }
                }
            ) {

                Text(
                    if (pickupId == null)
                        "Schedule Pickup"
                    else
                        "Update Pickup"
                )
            }
        }
    }
}