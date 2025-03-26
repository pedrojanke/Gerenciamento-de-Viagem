package com.example.gerenciamentodeviagem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.gerenciamentodeviagem.data.local.AppDatabase
import com.example.gerenciamentodeviagem.data.repository.UserRepository
import com.example.gerenciamentodeviagem.ui.navigation.AppNavigation
import com.example.gerenciamentodeviagem.ui.theme.TravelManagerTheme
import com.example.gerenciamentodeviagem.viewmodel.UserViewModel
import com.example.gerenciamentodeviagem.viewmodel.TravelViewModel
import com.example.gerenciamentodeviagem.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(db.userDao())
        val userViewModel = ViewModelProvider(this, ViewModelFactory(userRepository)).get(UserViewModel::class.java)
        val travelViewModel = ViewModelProvider(this).get(TravelViewModel::class.java)

        setContent {
            TravelManagerTheme {
                val navController = rememberNavController()
                AppNavigation(navController, userViewModel, travelViewModel)
            }
        }
    }
}
