package com.amaurysdelossantos.project.customComposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.amaurysdelossantos.project.navigation.RootComponent.Configuration
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import sunboundpages.composeapp.generated.resources.Res
import sunboundpages.composeapp.generated.resources.download
import sunboundpages.composeapp.generated.resources.library
import sunboundpages.composeapp.generated.resources.reading
import sunboundpages.composeapp.generated.resources.search
import sunboundpages.composeapp.generated.resources.settings

enum class BottomNavItem {
    Reading, Library, Downloads, Search, Settings
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selectedDestination: Configuration? = null,
    onNavigationSelected: (BottomNavItem) -> Unit = {}
) {
    val items = listOf(
        BottomBarItem("Reading", Res.drawable.reading, BottomNavItem.Reading),
        BottomBarItem("Library", Res.drawable.library, BottomNavItem.Library),
        BottomBarItem("Downloads", Res.drawable.download, BottomNavItem.Downloads),
        BottomBarItem("Search", Res.drawable.search, BottomNavItem.Search),
        BottomBarItem("Settings", Res.drawable.settings, BottomNavItem.Settings)
    )

    val backgroundColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(backgroundColor),
    ) {
        items.forEach { item ->
            val isSelected = when (selectedDestination) {
                Configuration.Reading -> item.destination == BottomNavItem.Reading
                Configuration.Library -> item.destination == BottomNavItem.Library
                Configuration.Downloads -> item.destination == BottomNavItem.Downloads
                Configuration.Search -> item.destination == BottomNavItem.Search
                Configuration.Settings -> item.destination == BottomNavItem.Settings
                else -> false
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onNavigationSelected(item.destination) },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(item.icon),
                    contentDescription = item.label,
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else contentColor.copy(alpha = 0.7f)
                    )
                )

                Text(
                    text = item.label,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else contentColor.copy(
                        alpha = 0.7f
                    ),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}


data class BottomBarItem(
    val label: String,
    val icon: DrawableResource,
    val destination: BottomNavItem
)

@Preview
@Composable
private fun PreviewBottomBar() {
    BottomBar()
}