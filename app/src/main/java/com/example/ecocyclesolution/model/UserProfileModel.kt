package com.example.ecocyclesolution.model

data class UserProfileModel(
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val dob: String = "",
    val profileImageUrl: String = "",
    val totalRecycledKg: Double = 0.0,
    val points: Int = 0,
    val treesSaved: Int = 0,
    val role: String = "user"
)