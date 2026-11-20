package com.example.lumis.view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocationCity
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.lumis.view.theme.LumisGreen
import com.example.lumis.view.theme.LumisGreenLight
import com.example.lumis.view.theme.LumisSurface
import com.example.lumis.view.theme.LumisTextSecondary

enum class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("Início", Icons.Rounded.Home, Icons.Outlined.Home),
    EXPLORAR("Explorar", Icons.Rounded.Search, Icons.Outlined.Search),
    AGENDA("Agenda", Icons.Rounded.CalendarMonth, Icons.Outlined.CalendarMonth),
    CIDADE("Cidade", Icons.Rounded.LocationCity, Icons.Outlined.LocationCity),
    PERFIL("Perfil", Icons.Rounded.Person, Icons.Outlined.Person)
}

@Composable
fun BottomNavBar(
    selected: NavItem,
    onSelect: (NavItem) -> Unit
) {
    Surface(
        color = LumisSurface,
        shadowElevation = 12.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .selectableGroup()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem.entries.forEach { item ->
                LumisNavDestination(
                    item = item,
                    selected = item == selected,
                    onClick = { if (item != selected) onSelect(item) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun LumisNavDestination(
    item: NavItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconColor by animateColorAsState(
        targetValue = if (selected) LumisGreen else LumisTextSecondary,
        label = "navIconColor"
    )
    val containerColor by animateColorAsState(
        targetValue = if (selected) LumisGreenLight else Color.Transparent,
        label = "navContainerColor"
    )
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.08f else 1f,
        animationSpec = spring(dampingRatio = 0.68f, stiffness = 520f),
        label = "navIconScale"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .selectable(selected = selected, onClick = onClick, role = Role.Tab)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Box(
            modifier = Modifier
                .size(width = 44.dp, height = 34.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.label,
                tint = iconColor,
                modifier = Modifier
                    .size(22.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
            )
        }
        Text(
            text = item.label,
            color = iconColor,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1
        )
    }
}
