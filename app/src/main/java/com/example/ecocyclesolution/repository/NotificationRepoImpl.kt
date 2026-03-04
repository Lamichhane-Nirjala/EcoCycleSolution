package com.example.ecocyclesolution.repository

import com.example.ecocyclesolution.model.AppNotification
import com.google.firebase.database.*

class NotificationRepoImpl : NotificationRepo {

    private val ref =
        FirebaseDatabase
            .getInstance()
            .getReference("notifications")

    override fun addNotification(
        notification: AppNotification
    ) {

        ref.child(notification.id)
            .setValue(notification)
    }

    override fun observeNotifications(
        userId: String,
        callback: (List<AppNotification>) -> Unit
    ) {

        ref.orderByChild("userId")
            .equalTo(userId)
            .addValueEventListener(
                object : ValueEventListener {

                    override fun onDataChange(
                        snapshot: DataSnapshot
                    ) {

                        val list =
                            snapshot.children.mapNotNull {
                                it.getValue(
                                    AppNotification::class.java
                                )
                            }

                        callback(list.sortedByDescending {
                            it.time
                        })
                    }

                    override fun onCancelled(
                        error: DatabaseError
                    ) {}
                })
    }
}