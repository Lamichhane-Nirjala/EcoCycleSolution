package com.example.ecocyclesolution.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.ecocyclesolution.model.ProductModel
import com.google.firebase.database.*
import java.util.UUID
import java.util.concurrent.CountDownLatch

class ProductRepoImpl : ProductRepo {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val productsRef: DatabaseReference = database.getReference("products")

    override fun addProduct(
        productId: String,
        product: ProductModel,
        callback: (Boolean, String) -> Unit
    ) {
        productsRef.child(productId).setValue(product)
            .addOnSuccessListener {
                callback(true, "Product added successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to add product")
            }
    }

    override fun getAllProducts(callback: (Boolean, String, List<ProductModel>) -> Unit) {
        productsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<ProductModel>()
                for (child in snapshot.children) {
                    val product = child.getValue(ProductModel::class.java)
                    product?.let { productList.add(it) }
                }
                callback(true, "Products loaded", productList)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }
        })
    }

    override fun getProductById(
        productId: String,
        callback: (Boolean, String, ProductModel?) -> Unit
    ) {
        productsRef.child(productId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(ProductModel::class.java)
                if (product != null) {
                    callback(true, "Product found", product)
                } else {
                    callback(false, "Product not found", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    override fun updateProduct(
        productId: String,
        product: ProductModel,
        callback: (Boolean, String) -> Unit
    ) {
        productsRef.child(productId).setValue(product)
            .addOnSuccessListener {
                callback(true, "Product updated successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to update product")
            }
    }

    override fun deleteProduct(
        productId: String,
        callback: (Boolean, String) -> Unit
    ) {
        productsRef.child(productId).removeValue()
            .addOnSuccessListener {
                callback(true, "Product deleted successfully")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Failed to delete product")
            }
    }

    override fun uploadProduct(
        context: Context,
        imageUri: Uri
    ): String? {
        var uploadedUrl: String? = null
        val latch = CountDownLatch(1)

        val requestId = UUID.randomUUID().toString()

        MediaManager.get().upload(imageUri)
            .unsigned("ecocycle_mobile")  // Your unsigned preset
            .option("folder", "ecocycle_products")
            .option("public_id", requestId)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String, resultData: Map<*, *>?) {
                    uploadedUrl = resultData?.get("secure_url") as? String
                    latch.countDown()
                }

                override fun onError(requestId: String, error: ErrorInfo?) {
                    latch.countDown()
                }

                override fun onReschedule(requestId: String, error: ErrorInfo?) {}
            })
            .dispatch(context)

        try {
            latch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return uploadedUrl
    }

    override fun getFileNameFromUri(
        context: Context,
        uri: Uri  // ← Fixed: now Uri, not String
    ): String? {
        var fileName: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName ?: "uploaded_image"
    }
}