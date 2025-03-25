package com.example.gerenciamentodeviagem.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gerenciamentodeviagem.ui.screens.LoginScreen
import com.example.gerenciamentodeviagem.ui.screens.RegisterScreen
import com.example.gerenciamentodeviagem.viewmodel.UserViewModel

@Composable
fun AppNavigation(navController: NavHostController, userViewModel: UserViewModel) {
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, userViewModel) }
        composable("register") { RegisterScreen(navController, userViewModel) }
        composable("home") {}
    }
}
