package com.example.ecocyclesolution.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.ecocyclesolution.ui.profile.EditProfileScreen
import com.example.ecocyclesolution.ui.theme.EcoCycleSolutionTheme
import com.example.ecocyclesolution.viewModel.PickupViewModel

/* ================================================= */
/* ================= MAIN ACTIVITY ================= */
/* ================================================= */

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EcoCycleSolutionTheme {
                EcoCycleMain()
            }
        }
    }
}

/* ================================================= */
/* ================= MAIN APP ====================== */
/* ================================================= */

@Composable
fun EcoCycleMain() {

    val navController = rememberNavController()
    val pickupVM: PickupViewModel = viewModel()
    val context = LocalContext.current

    val bottomScreens = listOf(
        Screen.Home,
        Screen.Rewards,
        Screen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(

        /* Bottom Navigation */

        bottomBar = {

            if (currentRoute in bottomScreens.map { it.route }) {

                NavigationBar(
                    containerColor = Color(0xFF121212)
                ) {

                    bottomScreens.forEach { screen ->

                        NavigationBarItem(
                            selected = currentRoute == screen.route,

                            icon = {
                                Icon(
                                    screen.icon,
                                    contentDescription = screen.title,
                                    tint = Color.White
                                )
                            },

                            label = {
                                Text(
                                    screen.title,
                                    color = Color.White
                                )
                            },

                            onClick = {

                                navController.navigate(screen.route) {

                                    popUpTo(
                                        navController.graph
                                            .findStartDestination().id
                                    )

                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }

    ) { padding ->

        /* Navigation Host */

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFF121212))
        ) {

            /* Dashboard */

            composable(Screen.Home.route) {
                UserDashboardScreen(navController)
            }

            /* Tracking */

            composable("tracking") {
                TrackingScreen(
                    pickupVM = pickupVM,
                    navController = navController
                )
            }

            /* Rewards */

            composable(Screen.Rewards.route) {
                RewardScreen(navController)
            }

            /* Profile */

            composable(Screen.Profile.route) {

                ProfileScreen(

                    onNavigateToLogin = {

                        context.startActivity(
                            Intent(context, LoginActivity::class.java)
                        )
                    }
                )
            }

            /* Edit Profile */

            composable("editProfile") {
                EditProfileScreen(navController)
            }

            /* Schedule Pickup */

            composable("schedule_pickup") {
                SchedulePickupScreen(navController)
            }

            /* Recycling Tips */

            composable("recyclingTips") {
                RecyclingTipsScreen()
            }
        }
    }
}

/* ================================================= */
/* ================= ROUTES ======================== */
/* ================================================= */

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {

    object Home :
        Screen("dashboard", "Home", Icons.Default.Home)

    object Rewards :
        Screen("rewards", "Rewards", Icons.Default.Star)

    object Profile :
        Screen("profile", "Profile", Icons.Default.Person)
}

