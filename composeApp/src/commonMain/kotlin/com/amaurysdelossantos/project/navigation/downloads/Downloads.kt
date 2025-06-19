package com.amaurysdelossantos.project.navigation.downloads

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import sunboundpages.composeapp.generated.resources.Res
import sunboundpages.composeapp.generated.resources.chevron_right
import sunboundpages.composeapp.generated.resources.radar


@Composable
fun Downloads(
    component: DownloadsComponent,
    innerPadding: PaddingValues = PaddingValues()
) {
    val iconColor = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = Modifier.padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Downloads",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 25.sp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* TODO: Navigate to Recently Imported */ }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recently Imported",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(Res.drawable.chevron_right),
                    contentDescription = "Chevron",
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(iconColor)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(Res.drawable.radar),
            contentDescription = "No books to manage",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .size(100.dp),
            colorFilter = ColorFilter.tint(iconColor)
        )
    }
}
