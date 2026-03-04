package com.example.ecocyclesolution.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecocyclesolution.viewModel.PickupViewModel

@Composable
fun NotificationScreen(
    navController: NavController,
    pickupVM: PickupViewModel = viewModel()
) {

    val notifications =
        pickupVM.notifications

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
    ) {

        items(notifications) { notification ->

            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
            ) {

                Column(
                    Modifier.padding(16.dp)
                ) {

                    Text(notification.title)

                    Spacer(
                        Modifier.height(4.dp)
                    )

                    Text(notification.message)
                }
            }
        }
    }
}