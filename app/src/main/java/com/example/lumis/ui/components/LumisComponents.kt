package com.example.lumis.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.ui.theme.*

enum class LumisTab { SERVICOS, PLANOS }

@Composable
fun LumisTopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(LumisGreen)
            )
            Spacer(Modifier.width(8.dp))
            Text("Lumis", style = MaterialTheme.typography.titleLarge, color = LumisTextPrimary)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconCircleButton(icon = Icons.Outlined.Notifications)
            IconCircleButton(icon = Icons.Outlined.Person)
        }
    }
}

@Composable
private fun IconCircleButton(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(LumisBackground),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = LumisTextSecondary, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun LumisTabSelector(
    selected: LumisTab,
    onSelected: (LumisTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(50))
            .background(LumisBackground)
            .padding(4.dp)
    ) {
        TabPill(
            text = "Serviços",
            isSelected = selected == LumisTab.SERVICOS,
            modifier = Modifier.weight(1f),
            onClick = { onSelected(LumisTab.SERVICOS) }
        )
        TabPill(
            text = "Planos",
            isSelected = selected == LumisTab.PLANOS,
            modifier = Modifier.weight(1f),
            onClick = { onSelected(LumisTab.PLANOS) }
        )
    }
}

@Composable
private fun TabPill(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(if (isSelected) LumisGreen else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else LumisTextSecondary,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun LumisScreenHeader(
    label: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = LumisTeal
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = LumisTextPrimary
        )
    }
}

@Composable
fun LumisBottomBar(
    buttonText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    showArrow: Boolean = false,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(LumisSurface)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LumisGreen,
                disabledContainerColor = LumisDisabledToggle
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(buttonText, style = MaterialTheme.typography.labelLarge, color = Color.White)
            if (showArrow) {
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
        if (subtitle != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = LumisTextTertiary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LumisCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val borderColor = if (isSelected) LumisSelectedBorder else LumisBorder
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(LumisSurface)
            .border(width = if (isSelected) 1.5.dp else 1.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(16.dp),
        content = content
    )
}
