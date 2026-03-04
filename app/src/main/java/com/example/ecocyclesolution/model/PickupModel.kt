package com.example.ecocyclesolution.model

data class PickupModel(

    val pickupId: String = "",
    val userId: String = "",

    val wasteType: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = "",

    val status: String = "Pending",

    val driverId: String? = null,
    val driverName: String? = null,
    val driverVehicle: String? = null,
    val driverPhone: String? = null
)