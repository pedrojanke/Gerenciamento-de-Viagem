package com.example.gerenciamentodeviagem.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gerenciamentodeviagem.data.models.Travel
import com.example.gerenciamentodeviagem.data.models.TravelType
import com.example.gerenciamentodeviagem.data.utils.PreferencesManager
import com.example.gerenciamentodeviagem.data.utils.generateRoteiro
import com.example.gerenciamentodeviagem.viewmodel.TravelViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(navController: NavController, viewModel: TravelViewModel) {
    val context = LocalContext.current
    val userId = remember { PreferencesManager.getUserId(context) }

    LaunchedEffect(Unit) {
        if (userId != -1) {
            viewModel.loadTravels(userId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Travel") })
        },
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
                )
                BottomNavigationItem(
                    selected = false,
                    onClick = { navController.navigate("new_travel") },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Nova Viagem") }
                )
                BottomNavigationItem(
                    selected = false,
                    onClick = {},
                    icon = { Icon(Icons.Default.Info, contentDescription = "About") }
                )
            }
        }
    ) { padding ->
        TravelList(viewModel, navController, Modifier.padding(padding))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TravelList(viewModel: TravelViewModel, navController: NavController, modifier: Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(viewModel.travels, key = { it.id }) { travel ->
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                        viewModel.removeTravel(travel)
                        true
                    } else false
                }
            )

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                background = {
                    val color = when (dismissState.dismissDirection) {
                        DismissDirection.StartToEnd,
                        DismissDirection.EndToStart -> Color.Red
                        null -> Color.Transparent
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .background(color)
                    )
                },
                dismissContent = {
                    TravelItem(
                        travel = travel,
                        onClick = {},
                        onLongClick = {
                            navController.navigate("edit_travel/${travel.id}")
                        },
                        viewModel = viewModel
                    )
                }
            )
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun TravelItem(
    travel: Travel,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    viewModel: TravelViewModel
) {
    val context = LocalContext.current
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    val imageFileName = getImageForTravelType(travel.type)
    val imageBitmap = remember(imageFileName) {
        loadBitmapFromAssets(context, "images/$imageFileName")
    }

    var showDialog by remember { mutableStateOf(false) }
    var roteiro by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = { onLongClick() })
            }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            imageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Imagem da viagem",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 16.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = travel.destination,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Início: ${formatter.format(travel.startDate)}")
                travel.endDate?.let {
                    Text(text = "Fim: ${formatter.format(it)}")
                }
                Text(text = "Orçamento: ${currencyFormat.format(travel.budget)}")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                roteiro = travel.roteiroSalvo
                if (roteiro == null) {
                    // Vai gerar roteiro novo, mostra diálogo e loading
                    isLoading = true
                } else {
                    isLoading = false
                }
                showDialog = true
            }) {
                Text("Roteiro")
            }
        }
    }

    if (showDialog) {
        LaunchedEffect(showDialog, travel) {
            if (showDialog && roteiro == null) {
                val generatedRoteiro = generateRoteiro(travel)
                roteiro = generatedRoteiro
                isLoading = false
            }
        }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Roteiro da Viagem") },
            text = {
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    // Container fixo com altura máxima definida
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)  // FIXO para limitar o tamanho do diálogo
                    ) {
                        // Conteúdo rolável dentro do box fixo
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            item {
                                Text(
                                    text = roteiro ?: "Erro ao gerar o roteiro.",
                                    style = MaterialTheme.typography.body1
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (!isLoading && travel.roteiroSalvo == null) {
                    Button(onClick = {
                        travel.roteiroSalvo = roteiro
                        viewModel.updateTravel(travel)
                        showDialog = false
                    }) {
                        Text("Aceitar")
                    }
                }
            },
            dismissButton = {
                Row {
                    if (!isLoading && travel.roteiroSalvo == null) {
                        Button(onClick = {
                            isLoading = true
                            roteiro = null
                        }) {
                            Text("Gerar outro")
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { showDialog = false }) {
                        Text("Fechar")
                    }
                }
            }
        )
    }

}


fun getImageForTravelType(type: TravelType): String {
    return when (type) {
        TravelType.BUSINESS -> "trabalho.png"
        TravelType.LEISURE -> "lazer.png"
    }
}

fun loadBitmapFromAssets(context: Context, filePath: String) =
    runCatching {
        context.assets.open(filePath).use { BitmapFactory.decodeStream(it) }
    }.getOrNull()
