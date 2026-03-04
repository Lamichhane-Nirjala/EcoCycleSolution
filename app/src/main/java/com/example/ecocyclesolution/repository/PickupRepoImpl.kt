package com.example.ecocyclesolution.repository

import com.example.ecocyclesolution.model.PickupModel
import com.google.firebase.database.*

class PickupRepoImpl : PickupRepo {

    private val database: FirebaseDatabase =
        FirebaseDatabase.getInstance()

    private val pickupRef: DatabaseReference =
        database.getReference("pickups")

    private var pickupListener: ValueEventListener? = null


    /* ================= ADD PICKUP ================= */

    override fun addPickup(
        pickupId: String,
        pickup: PickupModel,
        callback: (Boolean, String) -> Unit
    ) {

        pickupRef.child(pickupId)
            .setValue(pickup)
            .addOnSuccessListener {
                callback(true, "Pickup Scheduled")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed")
            }
    }


    /* ================= GET USER PICKUPS ================= */

    override fun getUserPickups(
        userId: String,
        callback: (Boolean, String, List<PickupModel>) -> Unit
    ) {

        pickupListener?.let {
            pickupRef.removeEventListener(it)
        }

        pickupListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val pickupList = mutableListOf<PickupModel>()

                for (child in snapshot.children) {

                    val pickup =
                        child.getValue(PickupModel::class.java)
                            ?.copy(
                                pickupId = child.key ?: ""
                            )

                    pickup?.let {
                        pickupList.add(it)
                    }
                }

                callback(true, "Loaded", pickupList)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }
        }

        pickupRef.orderByChild("userId")
            .equalTo(userId)
            .addValueEventListener(pickupListener!!)
    }


    /* ================= UPDATE PICKUP ================= */

    override fun updatePickup(
        pickupId: String,
        wasteType: String,
        date: String,
        time: String,
        location: String,
        callback: (Boolean, String) -> Unit
    ) {

        val updates = mapOf(
            "wasteType" to wasteType,
            "date" to date,
            "time" to time,
            "location" to location
        )

        pickupRef.child(pickupId)
            .updateChildren(updates)
            .addOnSuccessListener {
                callback(true, "Pickup Updated")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Update Failed")
            }
    }


    /* ================= UPDATE STATUS ================= */

    override fun updatePickupStatus(
        pickupId: String,
        status: String,
        callback: (Boolean, String) -> Unit
    ) {

        pickupRef.child(pickupId)
            .child("status")
            .setValue(status)
            .addOnSuccessListener {
                callback(true, "Status Updated")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed")
            }
    }


    /* ================= DELETE PICKUP ================= */

    override fun deletePickup(
        pickupId: String,
        callback: (Boolean, String) -> Unit
    ) {

        pickupRef.child(pickupId)
            .removeValue()
            .addOnSuccessListener {
                callback(true, "Pickup Deleted")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed")
            }
    }
}