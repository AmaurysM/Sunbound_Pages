package com.amaurysdelossantos.project.navigation.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amaurysdelossantos.project.util.rememberDocumentManager
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import sunboundpages.composeapp.generated.resources.Res
import sunboundpages.composeapp.generated.resources.chevron_right
import sunboundpages.composeapp.generated.resources.database
import sunboundpages.composeapp.generated.resources.folder
import sunboundpages.composeapp.generated.resources.smartphone

@Composable
fun Library(
    component: LibraryComponent,
    innerPadding: PaddingValues = PaddingValues(),
) {
    //val books by bookDao.getAllBooks().collectAsState(initial = emptyList())
    val scrollState = rememberScrollState()
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val books = component.allBooks.collectAsState(initial = emptyList())

    //val scope = rememberCoroutineScope()

    val documentManager = rememberDocumentManager { sharedDoc ->
        sharedDoc?.let {
            component.handlePickedDocument(it)
        }
    }

    LaunchedEffect(Unit) {
        component.bindDocumentManager(documentManager)
    }

    Column(
        modifier = Modifier.padding(innerPadding)
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        if (!books.value.isEmpty()) {
            SectionTitle("Library")
            SettingsSection {
                SettingsItem(
                    iconRes = Res.drawable.smartphone,
                    title = "On My Device",
                    onClick = { component.onEvent(LibraryEvent.OnMyDevice) }
                )
            }
        }

        SectionTitle("Import Services")
        SettingsSection {
            SettingsItem(
                iconRes = Res.drawable.folder,
                title = "Import from Files",
                onClick = { component.onEvent(LibraryEvent.ImportFromFiles) }
            )
            SettingsItem(
                iconRes = Res.drawable.database,
                title = "Web Server",
                onClick = { component.onEvent(LibraryEvent.WebServer) }
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        ),
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@Composable
fun SettingsSection(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        content()
    }
}

@Composable
fun SettingsItem(
    iconRes: DrawableResource,
    title: String,
    onClick: () -> Unit
) {
    val iconColor = MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = "$title icon",
            modifier = Modifier
                .size(40.dp)
                .padding(end = 16.dp),
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(iconColor)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Image(
            painter = painterResource(Res.drawable.chevron_right),
            contentDescription = "Chevron",
            modifier = Modifier.size(20.dp),
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(iconColor)
        )
    }
}