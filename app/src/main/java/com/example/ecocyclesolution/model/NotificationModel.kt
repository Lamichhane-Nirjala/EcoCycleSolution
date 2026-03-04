package com.example.ecocyclesolution.model

data class AppNotification(

    val id: String = "",
    val title: String = "",
    val message: String = "",
    val time: Long = System.currentTimeMillis(),
    val userId: String = ""
)