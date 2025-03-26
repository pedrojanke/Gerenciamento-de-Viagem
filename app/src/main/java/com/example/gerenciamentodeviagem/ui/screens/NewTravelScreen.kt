package com.example.gerenciamentodeviagem.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gerenciamentodeviagem.data.models.Travel
import com.example.gerenciamentodeviagem.data.models.TravelType
import com.example.gerenciamentodeviagem.viewmodel.TravelViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NewTravelScreen(navController: NavController, viewModel: TravelViewModel) {
    var destination by remember { mutableStateOf("") }
    var travelType by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    fun showDatePicker(onDateSelected: (String) -> Unit) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                onDateSelected(formatter.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nova Viagem") }) },
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(selected = false, onClick = { navController.navigate("home") }, icon = { Icon(Icons.Default.Home, contentDescription = "Home") })
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)) {

            // Destino
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Destino") },
                modifier = Modifier.fillMaxWidth()
            )

            // Tipo de viagem
            OutlinedTextField(
                value = travelType,
                onValueChange = { travelType = it },
                label = { Text("Tipo (TRABALHO ou LAZER") },
                modifier = Modifier.fillMaxWidth()
            )

            // Data início
            OutlinedTextField(
                value = startDate,
                onValueChange = {},
                label = { Text("Início") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker { startDate = it } },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Selecionar data",
                        modifier = Modifier.clickable { showDatePicker { startDate = it } }
                    )
                },
                readOnly = true
            )

            // Data fim
            OutlinedTextField(
                value = endDate,
                onValueChange = {},
                label = { Text("Fim") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker { endDate = it } },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Selecionar data",
                        modifier = Modifier.clickable { showDatePicker { endDate = it } }
                    )
                },
                readOnly = true
            )

            // Orçamento
            CurrencyTextField(
                label = "Orçamento",
                value = budget,
                onValueChange = { budget = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão Salvar
            Button(
                onClick = {
                    if (destination.isNotEmpty() && startDate.isNotEmpty() && budget.isNotEmpty()) {
                        val start = formatter.parse(startDate)
                        val end = if (endDate.isNotEmpty()) formatter.parse(endDate) else null
                        val newTravel = Travel(
                            id = 0,
                            destination = destination,
                            type = if (travelType.uppercase() == "TRABALHO") TravelType.BUSINESS else TravelType.LEISURE,
                            startDate = start,
                            endDate = end,
                            budget = budget.replace("[^\\d,]".toRegex(), "").replace(",", ".").toDouble()
                        )
                        viewModel.addTravel(newTravel)
                        navController.navigate("home")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
            }
        }
    }
}

@Composable
fun CurrencyTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    var rawValue by remember { mutableStateOf(0L) }
    val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    OutlinedTextField(
        value = numberFormat.format(rawValue / 100.0),
        onValueChange = { newValue ->
            val cleanString = newValue.replace("[^\\d]".toRegex(), "")
            if (cleanString.isNotEmpty()) {
                rawValue = cleanString.toLong()
                onValueChange(cleanString)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {}
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

