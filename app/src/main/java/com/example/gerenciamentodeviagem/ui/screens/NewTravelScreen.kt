package com.example.gerenciamentodeviagem.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
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
import java.util.Calendar
import java.util.Locale

@Composable
fun NewTravelScreen(navController: NavController, viewModel: TravelViewModel) {
    var destination by remember { mutableStateOf("") }
    var travelType by remember { mutableStateOf("TRABALHO") }
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

            Spacer(modifier = Modifier.height(16.dp))

            // Tipo de viagem
            Text("Tipo de Viagem", style = MaterialTheme.typography.body1)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TravelTypeOption(
                    imageFileName = "trabalho.png",
                    selected = travelType == "TRABALHO",
                    onSelect = { travelType = "TRABALHO" }
                )

                TravelTypeOption(
                    imageFileName = "lazer.png",
                    selected = travelType == "LAZER",
                    onSelect = { travelType = "LAZER" }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                            type = if (travelType == "TRABALHO") TravelType.BUSINESS else TravelType.LEISURE,
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


// Componente para seleção do tipo de viagem
@Composable
fun TravelTypeOption(imageFileName: String, selected: Boolean, onSelect: () -> Unit) {
    val context = LocalContext.current
    val imageBitmap = remember(imageFileName) { loadBitmapFromAssets(context, "images/$imageFileName") }

    Column(
        modifier = Modifier
            .clickable { onSelect() }
            .padding(8.dp)
            .size(40.dp)
    ) {
        imageBitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize())
        }

        Spacer(modifier = Modifier.height(8.dp))

        RadioButton(
            selected = selected,
            onClick = onSelect,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        )
    }
}

// Função para carregar imagens da pasta assets
fun loadBitmapFromAssets(context: Context, filePath: String) =
    runCatching {
        context.assets.open(filePath).use { BitmapFactory.decodeStream(it) }
    }.getOrNull()

// Campo de texto para orçamento
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
