package com.example.gerenciamentodeviagem.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.gerenciamentodeviagem.ui.modelview.TravelViewModel
import com.example.travelapp.data.model.Travel
import java.util.Calendar


@Composable
fun NewTravelScreen(
    navController: NavController,
    viewModel: TravelViewModel,
    travelId: Int? = null
) {
    var destination by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("business") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf(0.0) }

    // Carregar viagem existente, mas agora com verificação de nulo
    if (travelId != null) {
        val travel = viewModel.getTravelById(travelId)
        travel?.let {
            destination = it.destination
            type = it.type
            startDate = it.startDate
            endDate = it.endDate ?: ""
            budget = it.budget
        }
    }

    val context = LocalContext.current
    var showToast by remember { mutableStateOf(false) }

    if (showToast) {
        // Exibe o Toast quando a variável 'showToast' é verdadeira
        Toast.makeText(context, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
        showToast = false  // Resetando o estado do Toast
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Destino")
        TextField(value = destination, onValueChange = { destination = it })

        Text("Tipo de Viagem")
        Row {
            RadioButton(selected = type == "business", onClick = { type = "business" })
            Text("Negócio")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = type == "leisure", onClick = { type = "leisure" })
            Text("Lazer")
        }

        Text("Data de Início")
        DatePicker(selectedDate = startDate) { date -> startDate = date }

        Text("Data de Término (Opcional)")
        DatePicker(selectedDate = endDate) { date -> endDate = date }

        Text("Orçamento")
        TextField(value = budget.toString(), onValueChange = { budget = it.toDoubleOrNull() ?: 0.0 })

        Button(onClick = {
            if (destination.isNotEmpty() && startDate.isNotEmpty()) {
                val travel = Travel(
                    id = travelId ?: 0,
                    destination = destination,
                    type = type,
                    startDate = startDate,
                    endDate = endDate.takeIf { it.isNotEmpty() },
                    budget = budget
                )
                if (travelId == null) {
                    viewModel.addTravel(travel)
                } else {
                    viewModel.updateTravel(travel)
                }
                navController.navigate("home")
            } else {
                // Alterando estado para exibir o Toast
                showToast = true
            }
        }) {
            Text("Salvar")
        }
    }
}

@Composable
fun DatePicker(selectedDate: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    TextField(
        value = selectedDate,
        onValueChange = { onDateSelected(it) },
        readOnly = true,
        label = { Text("Escolher Data") },
        modifier = Modifier.clickable {
            val datePickerDialog = DatePickerDialog(context, { _, year, month, dayOfMonth ->
                onDateSelected("$dayOfMonth/${month + 1}/$year")
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }
    )
}

