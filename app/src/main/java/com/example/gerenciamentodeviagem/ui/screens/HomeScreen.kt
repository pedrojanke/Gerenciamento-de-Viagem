package com.example.gerenciamentodeviagem.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gerenciamentodeviagem.data.models.Travel
import com.example.gerenciamentodeviagem.viewmodel.TravelViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController, viewModel: TravelViewModel) {

    Scaffold(
        topBar = { TopAppBar(title = { Text("Travel") }) },
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Home, contentDescription = "Home") })
                BottomNavigationItem(selected = false, onClick = { navController.navigate("new_travel") }, icon = { Icon(Icons.Default.Add, contentDescription = "Nova Viagem") })
                BottomNavigationItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Info, contentDescription = "About") })
            }
        }
    ) { padding ->
        TravelList(viewModel, navController, Modifier.padding(padding))
    }
}


@Composable
fun TravelList(viewModel: TravelViewModel, navController: NavController, modifier: Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(viewModel.travels) { travel ->
            TravelItem(travel, onClick = {
                // Navegar para editar
            })
        }
    }
}


@Composable
fun TravelItem(travel: Travel, onClick: () -> Unit) {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(travel.destination, fontWeight = FontWeight.Bold)
            Text("${formatter.format(travel.startDate)} - ${travel.endDate?.let { formatter.format(it) } ?: ""}")
            Text("Orçamento: ${currencyFormat.format(travel.budget)}", color = Color.Gray)
        }
    }
}


