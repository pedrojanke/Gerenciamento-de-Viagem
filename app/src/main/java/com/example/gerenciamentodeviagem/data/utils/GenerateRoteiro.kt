package com.example.gerenciamentodeviagem.data.utils

import com.example.gerenciamentodeviagem.data.models.Travel
import com.example.gerenciamentodeviagem.data.models.TravelType
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

suspend fun generateRoteiro(travel: Travel): String {
    val prompt = """
        Crie um roteiro de viagem para o seguinte plano:
        - Destino: ${travel.destination}
        - Data de início: ${SimpleDateFormat("dd/MM/yyyy").format(travel.startDate)}
        - Data de fim: ${travel.endDate?.let { SimpleDateFormat("dd/MM/yyyy").format(it) } ?: "Não especificado"}
        - Orçamento: R$${travel.budget}
        - Tipo: ${if (travel.type == TravelType.BUSINESS) "Trabalho" else "Lazer"}

        O roteiro deve incluir atividades principais por dia, sugestões de locais e respeitar o tipo de viagem.
    """.trimIndent()

    val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = ApiKeys.GEMINI_API_KEY
    )

    val response = generativeModel.generateContent(prompt)
    return response.text ?: "Não foi possível gerar o roteiro."
}

object ApiKeys {
    const val GEMINI_API_KEY = "AIzaSyCiOyyzVb8G4BNSKuiXqQ3sLzpyQlUvYPE"
}

