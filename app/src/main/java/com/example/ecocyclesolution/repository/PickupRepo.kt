package com.example.ecocyclesolution.repository


import com.example.ecocyclesolution.model.PickupModel


interface PickupRepo {

    fun addPickup(
        pickupId: String,
        pickup: PickupModel,
        callback: (Boolean, String) -> Unit
    )

    fun getUserPickups(
        userId: String,
        callback: (Boolean, String, List<PickupModel>) -> Unit
    )

    fun updatePickup(
        pickupId: String,
        wasteType: String,
        date: String,
        time: String,
        address: String,
        callback: (Boolean, String) -> Unit
    )

    fun updatePickupStatus(
        pickupId: String,
        status: String,
        callback: (Boolean, String) -> Unit
    )

    fun deletePickup(
        pickupId: String,
        callback: (Boolean, String) -> Unit
    )
}