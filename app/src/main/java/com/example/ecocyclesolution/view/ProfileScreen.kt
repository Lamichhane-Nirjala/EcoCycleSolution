package com.example.ecocyclesolution.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.ecocyclesolution.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onHelpClick: () -> Unit = {}
) {

    val profile by viewModel.profile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    val context = LocalContext.current

    /* ================= IMAGE PICKER ================= */

    val imageLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { viewModel.uploadProfilePicture(it) }
        }

    /* ================= ERROR TOAST ================= */

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    /* ================= LOADING ================= */

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    /* ================= NULL PROFILE ================= */

    val user = profile

    if (user == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Profile not available",
                color = Color.White,
                fontSize = 18.sp
            )
        }
        return
    }

    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(user.fullName) }
    var phone by remember { mutableStateOf(user.phone) }
    var dob by remember { mutableStateOf(user.dob) }

    Scaffold(
        containerColor = Color(0xFF121212)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(30.dp))

            /* ================= PROFILE IMAGE ================= */

            Box(contentAlignment = Alignment.BottomEnd) {

                AsyncImage(
                    model = user.profileImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFF00C853), CircleShape),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { imageLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF00C853), CircleShape)
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            /* ================= PROFILE INFO ================= */

            if (isEditing) {

                ProfileField("Full Name", name) { name = it }
                ProfileField("Phone", phone) { phone = it }
                ProfileField("DOB", dob) { dob = it }

                Spacer(Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                    Button(onClick = {
                        viewModel.updateProfile(
                            mapOf(
                                "fullName" to name,
                                "phone" to phone,
                                "dob" to dob
                            )
                        )
                        isEditing = false
                    }) {
                        Text("Save")
                    }

                    OutlinedButton(
                        onClick = { isEditing = false }
                    ) {
                        Text("Cancel")
                    }
                }

            } else {

                Text(
                    user.fullName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(user.email, color = Color.LightGray)

                if (user.phone.isNotBlank())
                    Text(user.phone, color = Color.Gray)

                if (user.dob.isNotBlank())
                    Text(user.dob, color = Color.Gray)

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { isEditing = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00C853)
                    )
                ) {
                    Icon(Icons.Default.Edit, null)
                    Spacer(Modifier.width(6.dp))
                    Text("Edit Profile")
                }
            }

            Spacer(Modifier.height(40.dp))

            /* ================= STATS ================= */

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                StatCard(
                    Icons.Default.Recycling,
                    "${user.totalRecycledKg.toInt()} KG",
                    "Saved",
                    Color.Green
                )

                StatCard(
                    Icons.Default.EmojiEvents,
                    viewModel.computeRank(user.totalRecycledKg),
                    "Rank",
                    Color.Yellow
                )
            }

            Spacer(Modifier.height(40.dp))

            Text(
                "Settings",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )

            SettingsRow(Icons.Default.Notifications,"Notifications",onNotificationsClick)
            SettingsRow(Icons.Default.Lock,"Privacy",onPrivacyClick)
            SettingsRow(Icons.AutoMirrored.Filled.Help,"Help & Support",onHelpClick)

            Spacer(Modifier.height(40.dp))

            Button(
                onClick = {
                    viewModel.signOut()
                    onNavigateToLogin()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Text("Logout")
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}

/* ================= COMPONENTS ================= */

@Composable
fun ProfileField(
    label: String,
    value: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}

@Composable
fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
    tint: Color
) {
    Card(
        modifier = Modifier.size(150.dp, 100.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )
    ) {
        Column(
            Modifier.fillMaxSize(),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = tint)
            Text(value, color = Color.White)
            Text(label, color = Color.Gray)
        }
    }
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {

        Row(
            Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = Color.Green)
            Spacer(Modifier.width(12.dp))
            Text(title, color = Color.White)
            Spacer(Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos,null,tint = Color.Gray)
        }

        HorizontalDivider(color = Color.DarkGray)
    }
}