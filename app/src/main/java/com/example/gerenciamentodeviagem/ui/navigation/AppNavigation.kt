package com.example.gerenciamentodeviagem.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gerenciamentodeviagem.ui.screens.EditTravelScreen
import com.example.gerenciamentodeviagem.ui.screens.HomeScreen
import com.example.gerenciamentodeviagem.ui.screens.LoginScreen
import com.example.gerenciamentodeviagem.ui.screens.NewTravelScreen
import com.example.gerenciamentodeviagem.ui.screens.RegisterScreen
import com.example.gerenciamentodeviagem.viewmodel.UserViewModel
import com.example.gerenciamentodeviagem.viewmodel.TravelViewModel

@Composable
fun AppNavigation(navController: NavHostController,
                  userViewModel: UserViewModel,
                  travelViewModel: TravelViewModel
) {
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, userViewModel) }
        composable("register") { RegisterScreen(navController, userViewModel) }
        composable("home") { HomeScreen(navController, travelViewModel) }
        composable("new_travel") { NewTravelScreen(navController, travelViewModel) }
        composable(
            "edit_travel/{travelId}",
            arguments = listOf(navArgument("travelId") { type = NavType.StringType })
        ) { backStackEntry ->
            val travelId = backStackEntry.arguments?.getString("travelId")
            travelId?.let {
                EditTravelScreen(navController = navController, viewModel = travelViewModel, travelId = it)
            }
        }
    }
}
