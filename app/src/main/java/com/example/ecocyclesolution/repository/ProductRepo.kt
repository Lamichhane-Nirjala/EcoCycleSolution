package com.example.ecocyclesolution.repository

import android.content.Context
import android.net.Uri
import com.example.ecocyclesolution.model.ProductModel

interface ProductRepo {

    fun addProduct(
        productId: String,
        product: ProductModel,
        callback: (Boolean, String) -> Unit
    )

    fun getAllProducts(callback: (Boolean, String, List<ProductModel>) -> Unit)

    fun getProductById(
        productId: String,
        callback: (Boolean, String, ProductModel?) -> Unit
    )

    fun updateProduct(
        productId: String,
        product: ProductModel,
        callback: (Boolean, String) -> Unit
    )

    fun deleteProduct(
        productId: String,
        callback: (Boolean, String) -> Unit
    )

    fun uploadProduct(
        context: Context,
        imageUri: Uri
    ): String?

    fun getFileNameFromUri(
        context: Context,
        uri: Uri  // ← Fixed: was url: String, now Uri
    ): String?
}