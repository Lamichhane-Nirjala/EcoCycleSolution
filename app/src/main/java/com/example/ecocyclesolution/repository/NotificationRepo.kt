package com.example.ecocyclesolution.repository

import com.example.ecocyclesolution.model.AppNotification

interface NotificationRepo {

    fun addNotification(
        notification: AppNotification
    )

    fun observeNotifications(
        userId: String,
        callback: (List<AppNotification>) -> Unit
    )
}