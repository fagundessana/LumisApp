package com.example.lumis.view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.view.theme.*

enum class LumisTab { SERVICOS, PLANOS }

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
    val background by animateColorAsState(
        if (isSelected) LumisGreen else Color.Transparent,
        label = "tabBackground"
    )
    val foreground by animateColorAsState(
        if (isSelected) Color(0xFF102018) else LumisTextSecondary,
        label = "tabForeground"
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(background)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = foreground,
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
    val borderColor by animateColorAsState(
        if (isSelected) LumisSelectedBorder else LumisBorder,
        label = "cardBorder"
    )
    val borderWidth by animateDpAsState(
        if (isSelected) 1.5.dp else 1.dp,
        label = "cardBorderWidth"
    )
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(LumisSurface)
            .border(width = borderWidth, color = borderColor, shape = RoundedCornerShape(16.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(16.dp),
        content = content
    )
}

@Composable
fun LumisTag(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(LumisGreenLight)
            .border(1.dp, LumisGreen.copy(alpha = 0.24f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(it, contentDescription = null, tint = LumisGreen, modifier = Modifier.size(15.dp))
        }
        Text(text, style = MaterialTheme.typography.labelSmall, color = LumisGreenDark)
    }
}

@Composable
fun LumisPageTitle(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    eyebrow: String? = null
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        eyebrow?.let { LumisTag(text = it) }
        Text(text = title, style = MaterialTheme.typography.headlineMedium, color = LumisTextPrimary)
        Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = LumisTextSecondary)
    }
}

@Composable
fun LumisPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    trailingIcon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier.heightIn(min = 54.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LumisGreen,
            contentColor = Color(0xFF102018),
            disabledContainerColor = LumisDisabledToggle
        )
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = Color(0xFF102018)
            )
        } else {
            Text(text, style = MaterialTheme.typography.labelLarge)
            trailingIcon?.let {
                Spacer(Modifier.width(8.dp))
                Icon(it, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun LumisSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.heightIn(min = 54.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, LumisGreen),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = LumisGreenDark)
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun LumisTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: (@Composable (() -> Unit))? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        modifier = modifier,
        leadingIcon = leadingIcon?.let { { Icon(it, contentDescription = null) } },
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        isError = isError,
        supportingText = supportingText?.let { { Text(it) } },
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LumisGreen,
            focusedLabelColor = LumisGreenDark,
            cursorColor = LumisGreen,
            focusedContainerColor = LumisSurface,
            unfocusedContainerColor = LumisSurface,
            unfocusedBorderColor = LumisBorder
        )
    )
}
