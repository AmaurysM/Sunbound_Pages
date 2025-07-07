package com.amaurysdelossantos.project.util.file_reader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.amaurysdelossantos.project.util.androidAppContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.readium.r2.shared.publication.Publication
import org.readium.r2.shared.publication.services.cover
import org.readium.r2.shared.util.http.DefaultHttpClient
import org.readium.r2.shared.util.asset.AssetRetriever
import org.readium.r2.shared.util.mediatype.MediaType
import org.readium.r2.streamer.PublicationOpener
import org.readium.r2.streamer.parser.DefaultPublicationParser
import java.io.ByteArrayOutputStream
import java.io.File
import org.readium.adapter.pdfium.document.PdfiumDocumentFactory
import org.readium.r2.shared.util.getOrElse
import org.readium.r2.shared.util.toUrl

actual suspend fun ReadEpub(path: String): EpubBook? = withContext(Dispatchers.IO) {
    val context = androidAppContext
    val file = File(context.filesDir, path)
    if (!file.exists()) return@withContext null

    try {
        val httpClient = DefaultHttpClient()
        val assetRetriever = AssetRetriever(
            contentResolver = context.contentResolver,
            httpClient = httpClient
        )
        val publicationOpener = PublicationOpener(
            publicationParser = DefaultPublicationParser(
                context,
                httpClient = httpClient,
                assetRetriever = assetRetriever,
                pdfFactory = PdfiumDocumentFactory(context)
            )
        )

        val url = file.toUrl()
        val asset = assetRetriever.retrieve(url, MediaType.EPUB).getOrNull() ?: return@withContext null

        val publication = publicationOpener.open(asset, allowUserInteraction = false).getOrNull() ?: return@withContext null

        val metadata = publication.metadata
        val coverBitmap: Bitmap? = publication.cover()
        val coverBytes: ByteArray? = coverBitmap?.let { bitmap ->
            ByteArrayOutputStream().use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.toByteArray()
            }
        }

        val chapters = publication.readingOrder.mapNotNull { link ->
            try {
                val resource = publication.get(link)
                val htmlBytes = resource?.read()?.getOrNull()
                val html = htmlBytes?.toString(Charsets.UTF_8)
                if (html != null) {
                    EpubChapter(
                        title = link.title ?: "Untitled Chapter",
                        htmlContent = html
                    )
                } else null
            } catch (e: Exception) {
                null
            }
        }

        return@withContext EpubBook(
            title = metadata.title?: "Untitled",
            author = metadata.authors.joinToString(" ") { it.name },
            coverImageBytes = coverBytes,
            chapters = chapters
        )

    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
