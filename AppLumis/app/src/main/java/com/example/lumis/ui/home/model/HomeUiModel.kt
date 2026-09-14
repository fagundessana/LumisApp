// Modelos de dados da Home (o "formato" das informações da tela).
// O grupo separou assim pra não ficar texto solto espalhado nos componentes.

package com.example.lumis.ui.home.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Iron
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material.icons.filled.Window
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.lumis.R

// Uma bolinha de categoria (ex: "Limpeza").
data class Category(val id: String, val label: String, val icon: ImageVector)

// Um quadradinho da grade "Serviços populares".
// isHighlight = true deixa ele verde-escuro (é o "Em expansão" do protótipo).
data class PopularService(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val isHighlight: Boolean = false
)

// Um card de profissional recomendado (foto, nota, preço...).
data class Professional(
    val id: String,
    val title: String,
    val rating: String, // ex: "4.8"
    val ratingCount: String, // ex: "2.3K"
    val duration: String, // ex: "5 horas"
    val price: String, // ex: "R$ 99,90"
    val discountBadge: String, // ex: "10% OFF"
    // Se um dia vier foto da internet, é só passar o link aqui.
    // Se ficar vazio, o app mostra a imagem placeholder que tá no drawable.
    val imageUrl: String? = null,
    val placeholderRes: Int,
    val isFavorite: Boolean = false // coraçãozinho preenchido ou não
)

// Dados falsos (mock) que o grupo usou pra montar a tela antes do backend ficar pronto.
object HomePreviewData {
    val categories = listOf(
        Category("limpeza", "Limpeza", Icons.Filled.WaterDrop),
        Category("residencial", "Residencial", Icons.Filled.Home),
        Category("comercial", "Comercial", Icons.Filled.Business)
    )

    val popularServices = listOf(
        PopularService("casas", "Casas e aptos", Icons.Filled.Home),
        PopularService("escritorios", "Escritórios", Icons.Filled.Business),
        PopularService("posreforma", "Pós-reforma", Icons.Filled.Iron),
        PopularService("sofas", "Sofás e tapetes", Icons.Filled.Weekend),
        PopularService("vidros", "Vidros e fachadas", Icons.Filled.Window),
        PopularService("geral", "Limpeza geral", Icons.Filled.CleaningServices),
        PopularService("empresas", "Empresas", Icons.Filled.Apartment),
        PopularService("expansao", "Em expansão", Icons.Filled.Close, isHighlight = true)
    )

    val recommended = listOf(
        Professional(
            id = "1",
            title = "Limpeza Residencial",
            rating = "4.8",
            ratingCount = "2.3K",
            duration = "5 horas",
            price = "R$ 99,90",
            discountBadge = "10% OFF",
            placeholderRes = R.drawable.placeholder_clean_1
        ),
        Professional(
            id = "2",
            title = "Limpeza Comercial",
            rating = "4.9",
            ratingCount = "150",
            duration = "5 horas",
            price = "R$ 199,90",
            discountBadge = "15% OFF",
            placeholderRes = R.drawable.placeholder_clean_2,
            isFavorite = true
        )
    )
}
