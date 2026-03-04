package com.example.ecocyclesolution.viewModel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecocyclesolution.model.ProductModel
import com.example.ecocyclesolution.repository.ProductRepoImpl
import com.example.ecocyclesolution.repository.UserRepoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel : ViewModel() {

    private val repo = ProductRepoImpl()
    private val userRepo = UserRepoImpl()

    // UI state
    var isUploading = mutableStateOf(false)
    var uploadedUrl = mutableStateOf("")
    var isLoadingProducts = mutableStateOf(false)
    var products = mutableStateOf<List<ProductModel>>(emptyList())
    var errorMessage = mutableStateOf<String?>(null)

    // Upload image on background thread
    fun uploadImage(context: Context, imageUri: Uri) {
        isUploading.value = true
        errorMessage.value = null

        viewModelScope.launch {
            val url = withContext(Dispatchers.IO) {
                repo.uploadProduct(context, imageUri)
            }

            if (!url.isNullOrEmpty()) {
                uploadedUrl.value = url
            } else {
                errorMessage.value = "Image upload failed"
            }

            isUploading.value = false
        }
    }

    // Add product to Firebase
    fun addProduct(
        name: String,
        description: String,
        pointsPrice: Int,
        imageUrl: String,
        onComplete: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        if (name.isBlank() || description.isBlank() || pointsPrice <= 0 || imageUrl.isBlank()) {
            onComplete(false, "Please fill all fields")
            return
        }

        val productId = System.currentTimeMillis().toString()

        val currentUserId = userRepo.getCurrentUser()?.uid ?: ""

        val product = ProductModel(
            id = productId,
            name = name,
            description = description,
            pointsPrice = pointsPrice,
            imageUrl = imageUrl,
            userId = currentUserId,
            status = "Pending"
        )

        repo.addProduct(productId, product) { success, message ->
            onComplete(success, message)
            if (success) {
                observeMyProducts()
            }
        }
    }

    // Load ALL products (Admin use)
    fun loadAllProducts() {
        isLoadingProducts.value = true
        errorMessage.value = null

        repo.getAllProducts { success, _, productList ->
            isLoadingProducts.value = false

            if (success) {
                products.value = productList
            } else {
                errorMessage.value = "Failed to load products"
                products.value = emptyList()
            }
        }
    }

    // USER SIDE ONLY — LOAD CURRENT USER PRODUCTS
    fun observeMyProducts() {

        val uid = userRepo.getCurrentUser()?.uid ?: return

        repo.getAllProducts { success, _, list ->

            if (success) {
                products.value = list.filter { it.userId == uid }
            }
        }
    }

    // Reset after successful add
    fun resetUpload() {
        uploadedUrl.value = ""
        isUploading.value = false
        errorMessage.value = null
    }
}