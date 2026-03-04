package com.example.ecocyclesolution.repository

import com.example.ecocyclesolution.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepo {

    fun login(
        email: String,
        password: String,
        callback: (success: Boolean, message: String) -> Unit
    )

    fun register(
        email: String,
        password: String,
        callback: (success: Boolean, message: String, userId: String?) -> Unit
    )

    fun addUserToDatabase(
        userId: String,
        model: UserModel,
        callback: (success: Boolean, message: String) -> Unit
    )

    fun updateProfile(
        userId: String,
        model: UserModel,
        callback: (success: Boolean, message: String) -> Unit
    )

    fun getUserById(
        userId: String,
        callback: (success: Boolean, message: String, user: UserModel?) -> Unit
    )

    fun getCurrentUser(): FirebaseUser?

    fun logOut(
        callback: (Boolean, String) -> Unit
    )

    fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    )
    fun updateEcoPoints(
        userId: String,
        points: Int,
        recycledKg: Double
    )

}