package com.amaurysdelossantos.project.navigation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sunboundpages.composeapp.generated.resources.Res
import sunboundpages.composeapp.generated.resources.chevron_right
import sunboundpages.composeapp.generated.resources.library
import sunboundpages.composeapp.generated.resources.manage_sun
import sunboundpages.composeapp.generated.resources.reader
import sunboundpages.composeapp.generated.resources.reading
import sunboundpages.composeapp.generated.resources.theme
import sunboundpages.composeapp.generated.resources.translate

@Composable
fun Settings(
    component: SettingsComponent,
    innerPadding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = Modifier.padding(innerPadding)
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {

        ProfileSection()

        SettingsSection(
            title = "GENERAL",
            items = listOf(
                SettingsItem("Reading Now", Res.drawable.reading),
                SettingsItem("Library", Res.drawable.library),
                SettingsItem("Readers", Res.drawable.reader)
            )
        )

        SettingsSection(
            title = "APPEARANCE",
            items = listOf(
                SettingsItem("Theme", Res.drawable.theme),
                SettingsItem("Languages", Res.drawable.translate)
            )
        )

        SettingsSection(
            title = "ACCOUNT",
            items = listOf(
                SettingsItem("Manage Sun", Res.drawable.manage_sun)
            )
        )
    }
}

@Composable
fun ProfileSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO */ }
            .padding(vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.library),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(60.dp)
                .background(Color.LightGray, CircleShape)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "Sign In",
                fontSize = 18.sp
            )
            Text(
                text = "Sync your reading session and keep track of your reads",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SettingsSection(title: String, items: List<SettingsItem>) {
    Column {
        Text(
            text = title,
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 1.dp
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    SettingItemRow(item)
                    if (index != items.lastIndex) {
                        Divider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

data class SettingsItem(
    val title: String,
    val icon: DrawableResource
)

@Composable
fun SettingItemRow(item: SettingsItem) {
    val iconTint = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { /* TODO: Navigate */ }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(item.icon),
            contentDescription = item.title,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(iconTint)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = item.title,
            fontSize = 16.sp,
            color = iconTint,
            modifier = Modifier.weight(1f)
        )

        Image(
            painter = painterResource(Res.drawable.chevron_right),
            contentDescription = "Open",
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(iconTint)
        )
    }
}
