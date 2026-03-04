package com.example.ecocyclesolution.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecocyclesolution.model.UserProfileModel
import com.example.ecocyclesolution.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repo =
        ProfileRepository(application.applicationContext)

    /* ================= PROFILE STATE ================= */

    private val _profile =
        MutableStateFlow<UserProfileModel?>(null)

    val profile: StateFlow<UserProfileModel?> = _profile

    val isLoading = MutableStateFlow(true)
    val errorMessage = MutableStateFlow<String?>(null)

    init {
        observeProfile()
    }

    /* ================================================= */
    /* OBSERVE PROFILE FROM FIRESTORE */
    /* ================================================= */

    private fun observeProfile() {

        viewModelScope.launch {

            repo.observeCurrentUserProfile()
                .collect { user ->

                    _profile.value = user
                    isLoading.value = false
                }
        }
    }

    /* ================================================= */
    /* UPLOAD PROFILE IMAGE */
    /* ================================================= */

    fun uploadProfilePicture(uri: Uri) {

        viewModelScope.launch {

            isLoading.value = true

            repo.uploadProfilePicture(uri)
                .onFailure {
                    errorMessage.value = it.message
                }

            isLoading.value = false
        }
    }

    /* ================================================= */
    /* UPDATE PROFILE */
    /* ================================================= */

    fun updateProfile(
        updates: Map<String, Any?>
    ) {

        viewModelScope.launch {

            isLoading.value = true

            repo.updateProfile(updates)
                .onFailure {
                    errorMessage.value = it.message
                }

            isLoading.value = false
        }
    }

    /* ================================================= */
    /* CLEAR PROFILE */
    /* ================================================= */

    fun clearProfileInfo() {

        viewModelScope.launch {
            repo.clearProfile()
        }
    }

    /* ================================================= */
    /* LOGOUT */
    /* ================================================= */

    fun signOut() {

        repo.signOut()
    }

    /* ================================================= */
    /* ECO RANK SYSTEM */
    /* ================================================= */

    fun computeRank(kg: Double): String =
        when {

            kg >= 1000 -> "Eco Legend 🌎"

            kg >= 500 -> "Eco Hero 🌳"

            kg >= 100 -> "Eco Warrior ♻️"

            else -> "Eco Starter 🌱"
        }
}

