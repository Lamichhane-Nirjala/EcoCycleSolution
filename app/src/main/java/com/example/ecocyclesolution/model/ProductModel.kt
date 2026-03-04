package com.example.ecocyclesolution.model

data class ProductModel(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val pointsPrice: Int = 0,
    val imageUrl: String = "",
    val userId: String = "",
    val status: String = "Pending",
    val createdAt: Long = System.currentTimeMillis()
)