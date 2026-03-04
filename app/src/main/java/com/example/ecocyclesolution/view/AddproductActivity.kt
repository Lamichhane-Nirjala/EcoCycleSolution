package com.example.ecocyclesolution.view

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.cloudinary.android.MediaManager
import com.example.ecocyclesolution.R
import com.example.ecocyclesolution.viewModel.ProductViewModel
import com.example.ecocyclesolution.ui.theme.EcoCycleSolutionTheme

class AddproductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val config = mapOf("cloud_name" to "dd71tz52c")
        try {
            MediaManager.init(this, config)
        } catch (e: Exception) {}

        setContent {
            EcoCycleSolutionTheme {
                AddProductScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(viewModel: ProductViewModel = viewModel()) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var pointsPrice by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Product", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth().height(100.dp))
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(value = pointsPrice, onValueChange = { if (it.all { c -> c.isDigit() }) pointsPrice = it }, label = { Text("Points Price") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(20.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Pick Image")
        }

        Spacer(Modifier.height(16.dp))

        imageUri?.let { uri ->
            AsyncImage(model = uri, contentDescription = "Preview", modifier = Modifier.size(200.dp))

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { viewModel.uploadImage(context, uri)  },
                enabled = !viewModel.isUploading.value
            ) {
                if (viewModel.isUploading.value) CircularProgressIndicator() else Text("Upload Image")
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                if (name.isBlank() || description.isBlank() || pointsPrice.isBlank() || viewModel.uploadedUrl.value.isBlank()) {
                    Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.addProduct(name, description, pointsPrice.toInt(), viewModel.uploadedUrl.value) { success, msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        if (success) (context as Activity).finish()
                    }
                }
            },
            enabled = viewModel.uploadedUrl.value.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Save Product")
        }
    }
}