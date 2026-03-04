package com.example.ecocyclesolution.model

data class UserModel(
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val dob: String = "",
    val password: String = "",  // Only used temporarily in ViewModel, never saved
    val role: String = "user",
    val points: Int = 0,
    val totalRecycledKg: Double = 0.0,
    val treesSaved: Int = 0,
    val profileImageUrl: String = ""
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "userId" to userId,
        "fullName" to fullName,
        "email" to email,
        "dob" to dob,
        "role" to role,
        "points" to points,
        "profileImageUrl" to profileImageUrl
    )
}