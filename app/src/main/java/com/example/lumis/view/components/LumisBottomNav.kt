package com.example.lumis.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.view.theme.LumisThemed
import com.example.lumis.view.theme.lumisThemed

enum class NavItem(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Outlined.Home),
    CIDADE("Cidade Consciente", Icons.Outlined.LocationCity),
    EXPLORAR("Explorar", Icons.Outlined.Search),
    AGENDA("Agenda", Icons.Outlined.CalendarMonth),
    PERFIL("Perfil", Icons.Outlined.Person)
}

@Composable
fun BottomNavBar(
    selected: NavItem,
    onSelect: (NavItem) -> Unit
) {
    Surface(
        color = LumisThemed.Surface,
        tonalElevation = 4.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            NavItem.values().forEach { item ->
                val isSelected = item == selected
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onSelect(item) }
                        .padding(horizontal = 4.dp)
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .offset(y = (-14).dp)
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(LumisThemed.Surface)
                                .border(2.dp, lumisThemed(Color(0xFFDFF6EC), Color(0xFF1F3A2E)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(lumisThemed(Color(0xFFDFF6EC), Color(0xFF1F3A2E))),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    tint = lumisThemed(Color(0xFF2FA57A), Color(0xFF7FDDB0)),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3FBF8F),
                            modifier = Modifier.offset(y = (-10).dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(6.dp))
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = lumisThemed(Color(0xFF8A8A8E), Color(0xFFA0A0A6)),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            color = lumisThemed(Color(0xFF8A8A8E), Color(0xFFA0A0A6))
                        )
                    }
                }
            }
        }
    }
}