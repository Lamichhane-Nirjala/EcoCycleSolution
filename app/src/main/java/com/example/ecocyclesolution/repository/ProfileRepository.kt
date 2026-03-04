package com.example.ecocyclesolution.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.example.ecocyclesolution.model.UserProfileModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class ProfileRepository(private val context: Context) {

    private val auth = FirebaseAuth.getInstance()

    private val db =
        FirebaseDatabase.getInstance()
            .reference.child("users")

    /* ================================================= */
    /* OBSERVE PROFILE */
    /* ================================================= */

    fun observeCurrentUserProfile():
            Flow<UserProfileModel?> = callbackFlow {

        val uid = auth.currentUser?.uid

        if (uid == null) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val listener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                val profile =
                    snapshot.getValue(
                        UserProfileModel::class.java
                    )

                trySend(profile)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(null)
            }
        }

        db.child(uid).addValueEventListener(listener)

        awaitClose {
            db.child(uid).removeEventListener(listener)
        }
    }

    /* ================================================= */
    /* BASE64 IMAGE UPLOAD (NO STORAGE NEEDED) */
    /* ================================================= */

    suspend fun uploadProfilePicture(
        uri: Uri
    ): Result<String> = runCatching {

        val uid =
            auth.currentUser?.uid
                ?: throw Exception("User not logged in")

        val inputStream =
            context.contentResolver
                .openInputStream(uri)

        val bitmap =
            BitmapFactory.decodeStream(inputStream)

        val outputStream =
            ByteArrayOutputStream()

        // ✅ compress image
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            40,
            outputStream
        )

        val bytes =
            outputStream.toByteArray()

        val base64Image =
            Base64.encodeToString(
                bytes,
                Base64.DEFAULT
            )

        db.child(uid)
            .child("profileImageUrl")
            .setValue(base64Image)
            .await()

        base64Image
    }

    /* ================================================= */
    /* UPDATE PROFILE */
    /* ================================================= */

    suspend fun updateProfile(
        updates: Map<String, Any?>
    ): Result<Unit> = runCatching {

        val uid =
            auth.currentUser?.uid
                ?: throw Exception("User not logged in")

        db.child(uid)
            .updateChildren(updates)
            .await()
    }

    suspend fun clearProfile(): Result<Unit> =
        runCatching {

            val uid =
                auth.currentUser?.uid
                    ?: throw Exception("User not logged in")

            db.child(uid).updateChildren(
                mapOf(
                    "fullName" to "",
                    "phone" to "",
                    "dob" to "",
                    "profileImageUrl" to ""
                )
            ).await()
        }

    fun signOut() {
        auth.signOut()
    }
}