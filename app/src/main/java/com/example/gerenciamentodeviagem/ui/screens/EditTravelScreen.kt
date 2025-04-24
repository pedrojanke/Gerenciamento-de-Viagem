package com.example.gerenciamentodeviagem.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gerenciamentodeviagem.data.models.Travel
import com.example.gerenciamentodeviagem.data.models.TravelType
import com.example.gerenciamentodeviagem.viewmodel.TravelViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditTravelScreen(navController: NavController, viewModel: TravelViewModel, travelId: String) {
    val travel = viewModel.getTravelById(travelId)

    var destination by remember { mutableStateOf(travel?.destination ?: "") }
    var startDate by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy").format(travel?.startDate)) }
    var endDate by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy").format(travel?.endDate)) }
    var budget by remember { mutableStateOf(travel?.budget?.toString() ?: "0.00") }

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    fun showDatePicker(onDateSelected: (String) -> Unit) {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                onDateSelected(formatter.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }


    Scaffold(
        topBar = { TopAppBar(title = { Text("Editar Viagem") }) },
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)) {

            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Destino") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            CurrencyTextField(
                label = "Orçamento",
                value = budget,
                onValueChange = { newValue -> budget = newValue }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (destination.isNotEmpty() && startDate.isNotEmpty() && budget.isNotEmpty()) {
                        val start = formatter.parse(startDate)
                        val end = if (endDate.isNotEmpty()) formatter.parse(endDate) else null
                        val budgetInReais = budget.toDouble() / 100

                        val updatedTravel = Travel(
                            id = travel?.id ?: 0,
                            destination = destination,
                            type = travel?.type ?: TravelType.BUSINESS,
                            startDate = start,
                            endDate = end,
                            budget = budgetInReais,
                            userId = travel?.userId ?: 0
                        )

                        viewModel.updateTravel(updatedTravel)
                        navController.navigate("home")
                    } else {
                        Log.i("EditTravelScreen", "⚠️ Campos obrigatórios não preenchidos!")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Alterações")
            }
        }
    }
}
