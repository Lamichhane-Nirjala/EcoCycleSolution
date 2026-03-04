package com.example.ecocyclesolution.repository

import com.example.ecocyclesolution.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class UserRepoImpl : UserRepo {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("users")


    /* ================= LOGIN ================= */

    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                callback(true, "Login successful")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Login failed")
            }
    }


    /* ================= REGISTER ================= */

    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String?) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->

                val uid = authResult.user?.uid

                callback(true, "Registration successful", uid)
            }
            .addOnFailureListener {

                callback(false, it.message ?: "Registration failed", null)
            }
    }


    /* ================= ADD USER ================= */

    override fun addUserToDatabase(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {

        usersRef.child(userId)
            .setValue(model)
            .addOnSuccessListener {
                callback(true, "Profile saved")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Save failed")
            }
    }


    /* ================= UPDATE PROFILE ================= */

    override fun updateProfile(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {

        usersRef.child(userId)
            .updateChildren(model.toMap())
            .addOnSuccessListener {
                callback(true, "Profile updated")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Update failed")
            }
    }


    /* ================= GET USER ================= */

    override fun getUserById(
        userId: String,
        callback: (Boolean, String, UserModel?) -> Unit
    ) {

        usersRef.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val user =
                        snapshot.getValue(UserModel::class.java)

                    callback(true, "User loaded", user)
                }

                override fun onCancelled(error: DatabaseError) {

                    callback(false, error.message, null)
                }
            })
    }


    /* ================= CURRENT USER ================= */

    override fun getCurrentUser(): FirebaseUser? =
        auth.currentUser


    /* ================= LOGOUT ================= */

    override fun logOut(
        callback: (Boolean, String) -> Unit
    ) {

        auth.signOut()
        callback(true, "Logged out successfully")
    }


    /* ================= FORGOT PASSWORD ================= */

    override fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                callback(true, "Reset email sent")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to send email")
            }
    }


    /* ================= ECO POINT SYSTEM ================= */

    override fun updateEcoPoints(
        userId: String,
        points: Int,
        recycledKg: Double
    ) {

        val userRef = usersRef.child(userId)

        userRef.get().addOnSuccessListener { snapshot ->

            val user =
                snapshot.getValue(UserModel::class.java)
                    ?: return@addOnSuccessListener

            val newPoints =
                user.points + points

            val newKg =
                user.totalRecycledKg + recycledKg

            val treesSaved =
                user.treesSaved + (recycledKg / 5).toInt()

            userRef.child("points").setValue(newPoints)
            userRef.child("totalRecycledKg").setValue(newKg)
            userRef.child("treesSaved").setValue(treesSaved)
        }
    }


    /* ================= UPDATE USER ================= */

    fun updateUser(
        user: UserModel,
        callback: (Boolean, String?) -> Unit = { _, _ -> }
    ) {

        val uid = user.userId

        usersRef
            .child(uid)
            .setValue(user)
            .addOnSuccessListener {

                callback(true, "User Updated")
            }
            .addOnFailureListener {

                callback(false, it.message)
            }
    }
}