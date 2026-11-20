// Modelos de dados da Home (o "formato" das informações da tela).
// O grupo separou assim pra não ficar texto solto espalhado nos componentes.

package com.example.lumis.view.screens.home.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Iron
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.annotation.StringRes
import com.example.lumis.R

// Uma bolinha de categoria (ex: "Limpeza").
data class Category(val id: String, @StringRes val labelRes: Int, val icon: ImageVector)

// Um quadradinho da grade "Serviços populares".
// isHighlight = true deixa ele verde-escuro (é o "Em expansão" do protótipo).
data class PopularService(
    val id: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    val isHighlight: Boolean = false
)

// Atalhos exibidos na Home. Cada item corresponde a um serviço realmente disponível.
object HomePreviewData {
    val categories = listOf(
        Category("limpeza", R.string.category_cleaning, Icons.Filled.WaterDrop),
        Category("residencial", R.string.category_residential, Icons.Filled.Home),
        Category("comercial", R.string.category_commercial, Icons.Filled.Business)
    )

    val popularServices = listOf(
        PopularService("Limpeza Padrão", R.string.service_standard, Icons.Filled.Home),
        PopularService("Limpeza Pesada", R.string.service_deep, Icons.Filled.CleaningServices),
        PopularService("Limpeza Pós-Mudança", R.string.service_moving, Icons.Filled.Iron),
        PopularService("Limpeza Airbnb / Temporada", R.string.service_airbnb, Icons.Filled.Apartment)
    )
}
