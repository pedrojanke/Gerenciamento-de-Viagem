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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.window.Dialog
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

    var showSuggestionDialog by remember { mutableStateOf(false) }
    var suggestionText by remember { mutableStateOf("") }

    // Estado para controlar quando gerar roteiro com sugestão
    var generateWithSuggestion by remember { mutableStateOf<Pair<Boolean, String>?>(null) }

    // Efeito para gerar roteiro normal ou com sugestão
    LaunchedEffect(generateWithSuggestion, showDialog) {
        if (showDialog && generateWithSuggestion != null) {
            isLoading = true
            roteiro = null
            val (withSuggestion, suggestion) = generateWithSuggestion!!
            roteiro = if (withSuggestion) {
                generateRoteiro(travel, suggestion)
            } else {
                generateRoteiro(travel)
            }
            isLoading = false
            generateWithSuggestion = null
        } else if (showDialog && roteiro == null && generateWithSuggestion == null) {
            // Gera roteiro padrão, quando abrir diálogo pela primeira vez
            isLoading = true
            roteiro = generateRoteiro(travel)
            isLoading = false
        }
    }

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
                    generateWithSuggestion = Pair(false, "")
                }
                showDialog = true
            }) {
                Text("Roteiro")
            }
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = {
            showDialog = false
            suggestionText = ""
        }) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colors.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .defaultMinSize(minHeight = 200.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Roteiro da Viagem",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 150.dp, max = 400.dp)
                                .padding(vertical = 8.dp)
                        ) {
                            LazyColumn {
                                item {
                                    Text(
                                        text = roteiro ?: "Erro ao gerar o roteiro.",
                                        style = MaterialTheme.typography.body1
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        if (!isLoading && travel.roteiroSalvo == null) {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = {
                                        travel.roteiroSalvo = roteiro
                                        viewModel.updateTravel(travel)
                                        showDialog = false
                                        suggestionText = ""
                                    },
                                    modifier = Modifier.widthIn(min = 100.dp)
                                ) {
                                    Text("Aceitar")
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        showSuggestionDialog = true
                                    },
                                    modifier = Modifier.widthIn(min = 100.dp)
                                ) {
                                    Text("Gerar outro")
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Button(
                            onClick = {
                                showDialog = false
                                suggestionText = ""
                            },
                            modifier = Modifier.widthIn(min = 100.dp)
                        ) {
                            Text("Fechar")
                        }
                    }
                }
            }
        }
    }

    //Sugestão para muydar o roteiro
    if (showSuggestionDialog) {
        Dialog(onDismissRequest = {
            showSuggestionDialog = false
            suggestionText = ""
        }) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colors.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "O que você não gostou no roteiro atual? O que deseja mudar?",
                        style = MaterialTheme.typography.h6
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = suggestionText,
                        onValueChange = { suggestionText = it },
                        placeholder = { Text("Digite sua sugestão aqui...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                showSuggestionDialog = false
                                suggestionText = ""
                            }
                        ) {
                            Text("Cancelar")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (suggestionText.isNotBlank()) {
                                    showSuggestionDialog = false
                                    generateWithSuggestion = Pair(true, suggestionText)
                                    suggestionText = ""
                                }
                            }
                        ) {
                            Text("Enviar")
                        }
                    }
                }
            }
        }
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
