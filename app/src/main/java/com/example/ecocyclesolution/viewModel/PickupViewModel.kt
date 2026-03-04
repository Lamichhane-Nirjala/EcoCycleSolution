package com.example.ecocyclesolution.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ecocyclesolution.model.PickupModel
import com.example.ecocyclesolution.model.AppNotification
import com.example.ecocyclesolution.repository.PickupRepoImpl
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

class PickupViewModel : ViewModel() {

    private val repo = PickupRepoImpl()

    var pickupList =
        mutableStateOf<List<PickupModel>>(emptyList())

    var message =
        mutableStateOf("")

    var notifications =
        mutableStateListOf<AppNotification>()


    /* ================= SCHEDULE PICKUP ================= */

    fun schedulePickup(
        waste: String,
        date: String,
        time: String,
        address: String
    ) {

        val uid =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        val pickupId = UUID.randomUUID().toString()

        val pickup = PickupModel(
            pickupId = pickupId,
            userId = uid,
            wasteType = waste,
            date = date,
            time = time,
            location = address,
            status = "Pending",

            driverId = "D1",
            driverName = "Ramesh Thapa",
            driverVehicle = "Eco Truck",
            driverPhone = "9800000000"
        )

        repo.addPickup(pickupId, pickup) { success, _ ->

            if (success) {

                pushNotification(
                    "Pickup Scheduled",
                    "Your pickup scheduled successfully ✅"
                )

                observeMyPickups()
            }
        }
    }


    /* ================= OBSERVE PICKUPS ================= */

    fun observeMyPickups() {

        val uid =
            FirebaseAuth.getInstance()
                .currentUser?.uid ?: return

        repo.getUserPickups(uid)
        { success, _, list ->

            if (success)
                pickupList.value = list
        }
    }


    /* ================= UPDATE STATUS ================= */

    fun updatePickupStatus(
        pickupId: String,
        status: String
    ) {

        repo.updatePickupStatus(
            pickupId,
            status
        ) { success, _ ->

            if (success) {

                if (status == "Completed") {

                    pushNotification(
                        "Pickup Completed",
                        "Eco points added successfully 🌱"
                    )
                }

                observeMyPickups()
            }
        }
    }


    /* ================= UPDATE PICKUP ================= */

    fun updatePickup(
        pickupId: String,
        waste: String,
        date: String,
        time: String,
        address: String
    ) {

        repo.updatePickup(
            pickupId,
            waste,
            date,
            time,
            address
        ) { success, _ ->

            if (success) {

                pushNotification(
                    "Pickup Updated",
                    "Pickup updated successfully ✏️"
                )

                observeMyPickups()
            }
        }
    }


    /* ================= DELETE PICKUP ================= */

    fun deletePickup(id: String) {

        repo.deletePickup(id) { success, _ ->

            if (success) {

                pushNotification(
                    "Pickup Deleted",
                    "Pickup removed successfully"
                )

                observeMyPickups()
            }
        }
    }


    /* ================= NOTIFICATIONS ================= */

    fun pushNotification(
        title: String,
        msg: String
    ) {

        notifications.add(
            0,
            AppNotification(
                title = title,
                message = msg
            )
        )

        message.value = msg
    }
}