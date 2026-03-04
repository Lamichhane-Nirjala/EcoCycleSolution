package com.example.ecocyclesolution.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecocyclesolution.model.UserModel
import com.example.ecocyclesolution.repository.UserRepoImpl
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repo = UserRepoImpl()

    val currentUser = mutableStateOf<UserModel?>(null)

    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val fullName = mutableStateOf("")
    val dob = mutableStateOf("")
    val confirmPassword = mutableStateOf("")

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    init {
        loadLoggedInUser()
    }

    /* ================= LOAD USER ================= */

    fun loadLoggedInUser() {

        val firebaseUser = repo.getCurrentUser() ?: return

        repo.getUserById(firebaseUser.uid) { success, _, user ->

            if (success && user != null) {
                currentUser.value = user
            }
        }
    }

    /* ================= LOGIN ================= */

    fun login() {

        if (email.value.isBlank() || password.value.isBlank()) {
            errorMessage.value = "Enter email & password"
            return
        }

        isLoading.value = true

        repo.login(
            email.value.trim(),
            password.value.trim()
        ) { success, message ->

            isLoading.value = false

            if (success) {

                viewModelScope.launch {

                    delay(500)

                    loadLoggedInUser()
                }

            } else {
                errorMessage.value = message
            }
        }
    }

    /* ================= SIGNUP ================= */

    fun signup(onSuccess: () -> Unit) {

        if (
            email.value.isBlank() ||
            password.value.isBlank() ||
            fullName.value.isBlank()
        ) {
            errorMessage.value = "Fill all fields"
            return
        }

        if (password.value != confirmPassword.value) {
            errorMessage.value = "Passwords mismatch"
            return
        }

        isLoading.value = true

        repo.register(
            email.value.trim(),
            password.value.trim()
        ) { success, message, uid ->

            if (success && uid != null) {

                val newUser = UserModel(
                    userId = uid,
                    fullName = fullName.value,
                    email = email.value,
                    dob = dob.value,
                    points = 0,
                    totalRecycledKg = 0.0,
                    treesSaved = 0
                )

                repo.addUserToDatabase(uid, newUser) { ok, _ ->

                    isLoading.value = false

                    if (ok) {
                        currentUser.value = newUser
                        onSuccess()
                    }
                }

            } else {
                isLoading.value = false
                errorMessage.value = message
            }
        }
    }

    /* ================= ADD ECO POINTS ================= */

    fun addEcoPoints(points: Int, recycledKg: Double) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        repo.updateEcoPoints(uid, points, recycledKg)

        viewModelScope.launch {

            delay(800)

            loadLoggedInUser()
        }
    }

    /* ================= REDEEM POINTS ================= */

    fun redeemPoints(cost: Int) {

        val user = currentUser.value ?: return

        if (user.points < cost) {
            errorMessage.value = "Not enough points"
            return
        }

        val updatedUser =
            user.copy(points = user.points - cost)

        repo.updateUser(updatedUser)

        currentUser.value = updatedUser
    }

    /* ================= LOGOUT ================= */

    fun logout() {

        repo.logOut { _, _ -> }

        currentUser.value = null
        clearFields()
    }

    private fun clearFields() {

        email.value = ""
        password.value = ""
        fullName.value = ""
        dob.value = ""
        confirmPassword.value = ""
    }
}